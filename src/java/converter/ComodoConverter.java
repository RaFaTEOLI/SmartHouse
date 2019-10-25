package converter;

import dao.DaoComodo;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Comodo;


@FacesConverter(forClass = Comodo.class, value = "comodoConverter")
public class ComodoConverter implements Converter {

     public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if ((value == null) || (value.length() == 0)) {
                return null;
            }
           DaoComodo dao = new DaoComodo();
            return dao.carregarUm(getID(value), Comodo.class);
        }

        Integer getID(String value) {
            Integer id;
            id = Integer.valueOf(value);
            return id;
        }

         String getStringID(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
       if (value == null) {
                return null;
            }

            if (value instanceof Comodo) {
                Comodo o = (Comodo) value;
                return getStringID(o.getComodoId());
            } else {
                throw new IllegalArgumentException("objeto " + value + " possui o tipo "
                        + value.getClass().getName()
                        + "; tipo esperado: " );
            }
    }

  }

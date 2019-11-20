package converter;

import dao.DaoRotina;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Rotina;


@FacesConverter(forClass = Rotina.class, value = "rotinaConverter")
public class RotinaConverter implements Converter {

     public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if ((value == null) || (value.length() == 0)) {
                return null;
            }
           DaoRotina dao = new DaoRotina();
            return dao.carregarUm(getID(value), Rotina.class);
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

            if (value instanceof Rotina) {
                Rotina o = (Rotina) value;
                return getStringID(o.getRotinaId());
            } else {
                throw new IllegalArgumentException("objeto " + value + " possui o tipo "
                        + value.getClass().getName()
                        + "; tipo esperado: " );
            }
    }

  }

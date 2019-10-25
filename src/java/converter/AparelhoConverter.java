package converter;

import dao.DaoAparelho;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Aparelho;


@FacesConverter(forClass = Aparelho.class, value = "aparelhoConverter")
public class AparelhoConverter implements Converter {

     public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if ((value == null) || (value.length() == 0)) {
                return null;
            }
           DaoAparelho dao = new DaoAparelho();
            return dao.carregarUm(getID(value), Aparelho.class);
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

            if (value instanceof Aparelho) {
                Aparelho o = (Aparelho) value;
                return getStringID(o.getAparelhoId());
            } else {
                throw new IllegalArgumentException("objeto " + value + " possui o tipo "
                        + value.getClass().getName()
                        + "; tipo esperado: " );
            }
    }

  }

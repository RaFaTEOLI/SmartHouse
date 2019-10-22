package converter;

import dao.DaoPessoa;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Pessoa;


@FacesConverter(forClass = Pessoa.class, value = "pessoaConverter")
public class PessoaConverter implements Converter {

     public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if ((value == null) || (value.length() == 0)) {
                return null;
            }
           DaoPessoa dao = new DaoPessoa();
            return dao.carregarUm(getID(value), Pessoa.class);
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

            if (value instanceof Pessoa) {
                Pessoa o = (Pessoa) value;
                return getStringID(o.getId());
            } else {
                throw new IllegalArgumentException("objeto " + value + " possui o tipo "
                        + value.getClass().getName()
                        + "; tipo esperado: " );
            }
    }

  }

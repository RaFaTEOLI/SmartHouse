package control;

import javax.el.ELResolver;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import control.CtrManterLogin;


public class Autorizador implements PhaseListener{

    public void afterPhase(PhaseEvent event) {
        CtrManterLogin cml = new CtrManterLogin();
        FacesContext fc = event.getFacesContext(); //pego o contexto do Faces
        String currentPage = fc.getViewRoot().getViewId(); // armazeno numa String o id da minha página que está sendo requisitada

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); // instancio um request

        if(request.getAttribute("usuarioLogado") == null) {
            NavigationHandler nh = fc.getApplication().getNavigationHandler(); // instancio um objeto de navegação
            nh.handleNavigation(fc, null, "login"); // passo o contexto do Faces, null para minha action, e loginPage como from-outcome
            fc.renderResponse(); // chamo a última fase do ciclo de vida
        }
    }

    public void beforePhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
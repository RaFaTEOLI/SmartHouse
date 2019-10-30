package model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Entity;

@ManagedBean (name="login")
@SessionScoped
@Entity
public class Login extends Pessoa {
        
}
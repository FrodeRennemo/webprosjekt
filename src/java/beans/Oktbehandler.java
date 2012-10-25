/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
/**
 *
 * @author Frode
 */
public class Oktbehandler implements java.io.Serializable {

    private Treningsokter oversikt = new Treningsokter();
    private List<OktStatus> tabelldata = Collections.synchronizedList(new ArrayList<OktStatus>());
    private Treningsokt tempOkt = new Treningsokt(); // midlertidig lager for ny transaksjon
    
    
}

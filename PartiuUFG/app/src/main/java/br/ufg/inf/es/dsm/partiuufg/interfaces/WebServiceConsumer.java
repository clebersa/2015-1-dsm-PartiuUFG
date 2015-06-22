package br.ufg.inf.es.dsm.partiuufg.interfaces;

import br.ufg.inf.es.dsm.partiuufg.model.WebServiceResponse;

/**
 * Created by alunoinf on 20/06/2015.
 */
public interface WebServiceConsumer {
    public void receiveResponse(WebServiceResponse response);
}

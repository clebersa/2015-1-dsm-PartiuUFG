package br.ufg.inf.es.dsm.partiuufg.dbModel;

import com.orm.SugarRecord;

/**
 * Created by bruno on 6/25/15.
 */
public class BusPointAccess extends SugarRecord<BusPointAccess> {
    Integer pointNumber;
    Integer busLineNumber;
    Long accessCount;

}

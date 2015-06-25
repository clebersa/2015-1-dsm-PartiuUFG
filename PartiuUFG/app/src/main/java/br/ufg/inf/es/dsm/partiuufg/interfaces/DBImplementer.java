package br.ufg.inf.es.dsm.partiuufg.interfaces;

/**
 * Created by Cleber on 25/06/2015.
 */
public interface DBImplementer<T> {
    public void onSelect(T t);
}

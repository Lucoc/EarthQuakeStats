package edu.itu.csc.earthquakestats.trie;

import java.util.List;

/**
 * Interface to auto complete city names where there was an earth quake.
 *
 * @author "Jigar Gosalia"
 *
 */
public interface AutoComplete {
    List<String> predict(String prefix, int count);
}
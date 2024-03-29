/*
 */

package com.chaudhuri.cataloggenerator;

import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import java.util.Comparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class ExpansionManifestComparator implements Comparator<ExpansionManifest> {
    private static final Logger log = LogManager.getLogger();

    @Override
    public int compare(ExpansionManifest em1, ExpansionManifest em2) {
        int result = 0;
        if (em1 == null) {
            if (em2 == null) {
                return 0;
            } else {
                return 1;
            }
        }
        
        // em1 is not null
        if (em2 == null) {
            return -1;
        }
        
        // both em1 and em2 are not null
        if (em1.getIdentifier() == null) {
            // em1 has no identifier
            return -1;
        } else {
            if (em2.getIdentifier() == null) {
                // em1 has, but em2 has no identifier
                return 1;
            }
            
            // both identifiers exist
            result = em1.getIdentifier().compareTo(em2.getIdentifier());
            if (result != 0) {
                return result;
            }
        }
        if (em1.getVersion() == null) {
            if (em2.getVersion() == null) {
                return 0;
            }
            return -1;
        } else {
            if (em2.getVersion()==null) {
                return -1;
            }
            result = em1.getVersion().compareTo(em2.getVersion());
            if (result != 0) {
                return result;
            }
        }
        if (em1.getDownloadUrl()== null) {
            return -1;
        } else {
            if (em2.getDownloadUrl() == null) {
                return -1;
            }
            result = em1.getDownloadUrl().compareTo(em2.getDownloadUrl());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

}

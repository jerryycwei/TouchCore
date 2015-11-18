package ca.mcgill.sel.core.weaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.InstantiationType;

/**
 * Stores information about the current dependency level for each of external aspect.
 * This class has methods that allows the RAMWeaver to weave the instantiation
 * starting with the external aspect that has the highest depth rank in the
 * dependency tree.
 * 
 * @author oalam
 */
public class ConcernWeavingInfo {
    
    private static int maxDepth;
    private Instantiation externalIns;
    private HashMap<Instantiation, Integer> ranks;
    private ArrayList<Instantiation> instantiations;
    /**
     * The constructor.
     *@param base the input aspect.
     */
    public ConcernWeavingInfo(Aspect base) {
        this.ranks = new HashMap<Instantiation, Integer>();
        this.instantiations = new ArrayList<Instantiation>();
    }
    /**
     *Returns the highest ranked instantiation.
     * @return highest ranked instantiation.
     */
    public Instantiation getHighestRankedAspect() {
        return externalIns;
    }
    /**
     *Returns the highest rank.
     *@param base the input aspect.
     * @return aspect the aspect to display
     */
    public int getHighestRank(Aspect base) {
        instantiations.clear();
        ranks.clear();
        instantiations.addAll(base.getInstantiations());
        getMaxDepth(base, 0);
        int highestr = -1;
        for (Instantiation inst : ranks.keySet()) {
            if (ranks.get(inst) > highestr) {
                highestr = ranks.get(inst);
                externalIns = inst;
            }
        }
        return highestr;
    }
    /**
     *Returns the maximum depth for a given aspect at a given depth.
     * @param aspect the input aspect.
     * @param depth the given depth of the aspect.
     *  
     */
    private void getMaxDepth(Aspect aspect, int depth) {
        List<Instantiation> insts = aspect.getInstantiations();
        for (Instantiation ins : insts) {
            maxDepth = -1;
            getInstantiationDepth(1, ins.getSource());
            ranks.put(ins, maxDepth);
        }
    }
    /**
     *Returns the maximum depth of an instantiation for a given aspect at a given depth.
     * @param external the input aspect.
     * @param depth the given depth of the aspect.
     *  
     */
    private void getInstantiationDepth(int depth, Aspect external) {
        for (Instantiation ins : external.getInstantiations()) {
            if (ins.getType() == InstantiationType.EXTENDS) {
                Aspect externalAspect = ins.getSource();
                getInstantiationDepth(depth + 1, externalAspect);
            }
        }
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }
    
}
package com.chipotlebanditos.spacebanditos.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public abstract class DialogTree {
    
    public final String text;
    
    public final List<Option> options;
    
    public DialogTree(String text, Option... options) {
        this.text = text;
        this.options = new ImmutableList.Builder<Option>().addAll(
                Arrays.asList(options)).build();
    }
    
    public abstract void applyEffect(); // TODO: add arguments for event state
                                        // (e.g. activity)
    
    public static abstract class Option implements Serializable {
        
        private static final long serialVersionUID = 1981759590425113114L;
        
        public final String label;
        
        public Option(String label) {
            this.label = label;
        }
        
        public abstract DialogTree select();
        
        public static class TerminalOption extends Option {
            
            private static final long serialVersionUID = 2702672333689848191L;
            
            public TerminalOption(String label) {
                super(label);
            }
            
            @Override
            public DialogTree select() {
                return null;
            }
            
        }
        
        public static class ContinuingOption extends Option {
            
            private static final long serialVersionUID = -3452956286853192838L;
            
            public final Map<Float, DialogTree> possibleOutcomes;
            
            public ContinuingOption(String label,
                    Map<Float, DialogTree> possibleOutcomes) {
                super(label);
                this.possibleOutcomes = new ImmutableMap.Builder<Float, DialogTree>()
                        .putAll(possibleOutcomes).build();
            }
            
            @Override
            public DialogTree select() {
                float r = new Random().nextFloat();
                for (Entry<Float, DialogTree> e : possibleOutcomes.entrySet()) {
                    if (r < e.getKey()) {
                        return e.getValue();
                    } else {
                        r -= e.getKey();
                    }
                }
                throw new IllegalStateException(
                        "outcome probabilities do not add up to one");
            }
            
        }
        
    }
    
}

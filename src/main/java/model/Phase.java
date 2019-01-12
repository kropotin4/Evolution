package model;

import java.io.Serializable;

public enum Phase implements Serializable {
    DEALING{
        @Override
        public String toString() {
            return "Фаза бесполезная, Марком введенная";
        }
    },
    GROWTH{
        @Override
        public String toString() {
            return "Фаза роста";
        }
    },
    CALC_FODDER_BASE{
        @Override
        public String toString() {
            return "Фаза определения корма";
        }
    },
    EATING{
        @Override
        public String toString() {
            return "Фаза приема пищи";
        }
    },
    EXTINCTION{
        @Override
        public String toString() {
            return "Фаза внезапной смерти";
        }
    };
}

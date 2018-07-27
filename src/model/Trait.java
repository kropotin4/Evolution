package model;

/********************************
 * Перечесление всех свойств.
 *
 * Новые свойства добавлять сюда.
 *********************************/

public enum Trait {

    PREDATOR{
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Хищник";
        }
    },
    HIGH_BODY {
        @Override
        public int getHunger() {
            return 1;
        }

        @Override
        public String toString() {
            return "Большой";
        }
    },
    RUNNING {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Быстрый";
        }
    },
    MIMICRY {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Мимикрия";
        }
    },
    GRAZING {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Топотун";
        }
    },
    POISONOUS {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Ядовитый";
        }
    },
    TAIL_LOSS {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Отбрасование хвоста";
        }
    },
    COMMUNICATION {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Взаимодействие";
        }
    },
    HIBERNATION {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Спячка";
        }
    },
    SCAVENGER {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Падальщик";
        }
    },
    SYMBIOSYS {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Симбиоз";
        }
    },
    PIRACY {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Пиратство";
        }
    },
    COOPERATION {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Сотрудничество";
        }
    },
    BURROWING {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Норное";
        }
    },
    CAMOUFLAGE {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Камуфляж";
        }
    },
    SHARP_VISION {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Острое зрение";
        }
    },
    PARASITE {
        @Override
        public int getHunger() {
            return 2;
        }

        @Override
        public String toString() {
            return "Паразит";
        }
    },
    SWIMMING {
        @Override
        public int getHunger() {
            return 0;
        }

        @Override
        public String toString() {
            return "Водоплавающее";
        }
    };

    public abstract int getHunger();
}

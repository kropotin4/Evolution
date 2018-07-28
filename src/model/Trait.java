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
            return 1;
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
        public String toString() {
            return "Быстрый";
        }
    },
    MIMICRY {
        @Override
        public String toString() {
            return "Мимикрия";
        }
    },
    GRAZING {
        @Override
        public String toString() {
            return "Топотун";
        }
    },
    POISONOUS {
        @Override
        public String toString() {
            return "Ядовитый";
        }
    },
    TAIL_LOSS {
        @Override
        public String toString() {
            return "Отбрасование хвоста";
        }
    },
    COMMUNICATION {
        @Override
        public String toString() {
            return "Взаимодействие";
        }
    },
    HIBERNATION {
        @Override
        public String toString() {
            return "Спячка";
        }
    },
    SCAVENGER {
        @Override
        public String toString() {
            return "Падальщик";
        }
    },
    SYMBIOSYS {
        @Override
        public String toString() {
            return "Симбиоз";
        }
    },
    PIRACY {
        @Override
        public String toString() {
            return "Пиратство";
        }
    },
    COOPERATION {
        @Override
        public String toString() {
            return "Сотрудничество";
        }
    },
    BURROWING {
        @Override
        public String toString() {
            return "Норное";
        }
    },
    CAMOUFLAGE {
        @Override
        public String toString() {
            return "Камуфляж";
        }
    },
    SHARP_VISION {
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
        public String toString() {
            return "Водоплавающее";
        }
    },
    FAT_TISSUE{
        @Override
        public String toString() {
            return "Жировой запас";
        }
    };

    public int getHunger(){
        return 0;
    }
}

package model;

import java.io.Serializable;

/********************************
 * Перечесление всех свойств.
 *
 * Новые свойства добавлять сюда.
 *********************************/

public enum Trait implements Serializable {

    PREDATOR{ //Ok
        @Override
        public int getHunger() {
            return 1;
        }

        @Override
        public String toString() {
            return "Хищник";
        }
    },
    HIGH_BODY { //Ok
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
    GRAZING { //Ok (Только при взятии еды из кормовой базы)
        @Override
        public String toString() {
            return "Топотун";
        }
    },
    POISONOUS { //Ok
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
    HIBERNATION { // Ok
        @Override
        public String toString() {
            return "Спячка";
        }
    },
    SCAVENGER { //Ok
        @Override
        public String toString() {
            return "Падальщик";
        }
    },
    SYMBIOSIS {
        @Override
        public String toString() {
            return "Симбиоз";
        }
    },
    PIRACY { //Ok
        @Override
        public String toString() {
            return "Пиратство";
        }

        @Override
        public String getDescription() {
            return "Использовать это свойство в свою фазу питания. Получить" +
                    "единицу еды, забрав её у другого существа на столе, которое уже получало еду в этот ход, " +
                    "но еще не НАКОРМЛЕНО. Существо может использовать это свойсво только раз в ход.";
        }
    },
    COOPERATION {
        @Override
        public String toString() {
            return "Сотрудничество";
        }
    },
    BURROWING { // Ok
        @Override
        public String toString() {
            return "Норное";
        }
    },
    CAMOUFLAGE { //Ok
        @Override
        public String toString() {
            return "Камуфляж";
        }
    },
    SHARP_VISION { //Ok
        @Override
        public String toString() {
            return "Острое зрение";
        }
    },
    PARASITE { //Ok
        @Override
        public int getHunger() {
            return 2;
        }

        @Override
        public String toString() {
            return "Паразит";
        }
    },
    SWIMMING { //Ok
        @Override
        public String toString() {
            return "Водоплавающее";
        }
    },
    FAT_TISSUE{ //Ok
        @Override
        public String toString() {
            return "Жировой запас";
        }
    };

    int getHunger(){return 0;}

    public String getDescription(){
        return "Нет описания";
    }
}

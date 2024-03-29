package model;

import java.io.Serializable;

/******************************************
 * Enumerate of traits.
 *
 * @author akropotin
 ******************************************/
public enum Trait implements Serializable {

    PREDATOR{ //Ok
        @Override
        public int getHunger() {
            return 1;
        }

        @Override
        public String getDescription() {
            return "Позволяет атаковать других животных, если у них нет препятствующих этому защитных свойств.";
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
        public String getDescription() {
            return "Данное животное может быть атаковано только БОЛЬШИМ хищникомю. Увеличивает потребность в пище на 1.";
        }

        @Override
        public String toString() {
            return "Большой";
        }
    },
    RUNNING { //Ok
        @Override
        public String getDescription() {
            return "Когда это животное атаковано хищником, атака срывается с вероятностью 1/2. " +
                    "\nПри любом исходе, атаковавший хищник больше не сможет атаковать в этот ход.";
        }

        @Override
        public String toString() {
            return "Быстрый";
        }
    },
    MIMICRY { // Ok (Один раз за ход)
        @Override
        public String getDescription() {
            return "Когда это животное атаковано хищником, владелец животного должен перенаправить атаку хищника на " +
                    "\nдругое своё животное, которое этот хищник способен атаковать. Если таких животных нет, " +
                    "\nМИМИКРИЯ не работает.";
        }

        @Override
        public String toString() {
            return "Мимикрия";
        }
    },
    GRAZING { //Ok (Только при взятии еды из кормовой базы)
        @Override
        public String getDescription() {
            return "Если свойство активировано, животное уничтожает одну еду в кормовой базе." +
                    "\nМожно активировать сколько угодно животных с таким свойством в свою фазу питания.";

        }

        @Override
        public String toString() {
            return "Топотун";
        }
    },
    POISONOUS { //Ok
        @Override
        public String getDescription() {
            return "Хищник, съевший это животное, погибает в конце хода.";
        }

        @Override
        public String toString() {
            return "Ядовитый";
        }
    },
    TAIL_LOSS { //Ok
        @Override
        public String getDescription() {
            return "Когда животное с этим свойством атаковано хищником, следует сбросить это или другое имеющееся у " +
                    "\nживотного свойство. Хищник получает одну единицу пищи, а животное выживает.";
        }

        @Override
        public String toString() {
            return "Отбрасование хвоста";
        }
    },
    COMMUNICATION {
        @Override
        public String getDescription() {
            return "Парное свойство. Когда одно животное получает пищу из кормовой базы, другое живоное получает " +
                    "\nдоступ к кормовой базе вне очереди.";
        }

        @Override
        public String toString() {
            return "Взаимодействие";
        }
    },
    HIBERNATION { // Ok
        @Override
        public String getDescription() {
            return "Можно использовать в свою фазу питания. При использовании животное считается накормленным. " +
                    "\nЭто свойство нельзя использовать два хода подряд и в последний ход игры." +
                    "\nЭто свойство не защищает от атак.";
        }

        @Override
        public String toString() {
            return "Спячка";
        }
    },
    SCAVENGER { //Ok
        @Override
        public String getDescription() {
            return "Когда съедено другое животное, животное с таким свойством получает одну фишку еды. За одно " +
                    "\nсъеденное животное фишку еды может получить только один ПАДАЛЬЩИК. " +
                    "В случае, если у игрока, " +
                    "\nкоторому принадлежит ХИЩНИК, есть ПАДАЛЬЩИК, фишку еды получает животное этого игрока. " +
                    "\nВ обратном случае фишку еды получает ПАДАЛЬЩИК первого по очереди игрока от игрока, которому " +
                    "\nпринадлежит атаковавший ХИЩНИК. Свойство ПАДАЛЬЩИК нельзя сыграть на животное со свойством " +
                    "\nХИЩНИК и наоборот.";

        }

        @Override
        public String toString() {
            return "Падальщик";
        }
    },
    SYMBIOSIS { //Ok
        @Override
        public String getDescription() {
            return "Парное свойство. ПТИЦА не может быть атакована хищником, пока жив КРОКОДИЛ, " +
                    "\nно не может получать еду, если КРОКОДИЛ не накормлен.";

        }

        @Override
        public String toString() {
            return "Симбиоз";
        }
    },
    PIRACY { //Ok
        @Override
        public String getDescription() {
            return "Использовать в свою фазу питания. Получить фишку еды, забрав её у другого животного, которое" +
                    "\n уже получило хотя бы одну фишку еды, но ещё не накормлено. Животное может использовать " +
                    "\nсвойство ПИРАТСТВО только один раз за ход.";
        }

        @Override
        public String toString() {
            return "Пиратство";
        }
    },
    COOPERATION {
        @Override
        public String getDescription() {
            return "Парное свойство. Когда одно животное получает еду, второе может получает единицу еды.";
        }

        @Override
        public String toString() {
            return "Сотрудничество";
        }
    },
    BURROWING { // Ok
        @Override
        public String getDescription() {
            return "Когда животное НАКОРМЛЕНО, оно не может быть атаковано хищником.";
        }

        @Override
        public String toString() {
            return "Норное";
        }
    },
    CAMOUFLAGE { //Ok
        @Override
        public String getDescription() {
            return "Животное может быть атаковано только хищником со свойством ОСТРОЕ ЗРЕНИЕ.";
        }

        @Override
        public String toString() {
            return "Камуфляж";
        }
    },
    SHARP_VISION { //Ok
        @Override
        public String getDescription() {
            return "Позволяет атаковать животных со свойством КАМУФЛЯЖ.";

        }

        @Override
        public String toString() {
            return "Острое зрение";
        }
    },
    PARASITE { //Ok
        @Override
        public String getDescription() {
            return "Можно сыграть только на чужое животное. Добавляет две единицы потребности в пище.";

        }

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
        public String getDescription() {
            return "Может быть атаковано только хищником, имеющим свойство ВОДОПЛАВАЮЩЕЕ. " +
                    "\nХищник со свойством ВОДОПЛАВАЮЩЕЕ не может атаковать животное без свойства ВОДОПЛАВАЮЩЕ";
        }
        @Override
        public String toString() {
            return "Водоплавающее";
        }
    },
    FAT_TISSUE{ //Ok
        @Override
        public String getDescription() {
            return "Позволяет сохранть излишки еды на голодные времена. Пополнение жирового запаса происходит, " +
                    "\nкогда НАКОРМЛЕННОЕ животное, имеющее незаполненные ячейки ЖИРОВОГО ЗАПАСА получает еду. " +
                    "\nЕсли животное не накормлено, но имеет заполненный жировой запас, то в фазу питания можно " +
                    "\n\"конвертировать\" сколько угодно жира в еду. Можно класть несколько карт ЖИРОВОГО ЗАПАСА " +
                    "\nна одно животное.";

        }

        @Override
        public String toString() {
            return "Жировой запас";
        }
    };

    /**********************
     * Get need for food.
     * @return hunger
     * @see Card
     **********************/
    int getHunger(){return 0;}

    /**********************
     * Get description of trait.
     * @return String
     **********************/
    public String getDescription(){
        return "";
    }
    /**********************
     * Check pait trait.
     * @return boolean
     **********************/
    public static boolean isPairTrait(Trait trait){
        return (trait == Trait.COMMUNICATION
                || trait == Trait.COOPERATION
                || trait == Trait.SYMBIOSIS);
    }
}

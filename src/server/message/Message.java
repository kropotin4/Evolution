package server.message;

import model.*;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Message implements Serializable {

    //TODO: Через этот класс будут передоваться действия игроков + сообщения сервера => сделать это
    //Можно сделать несколько server.message.Message разных типов под разные действия

    Phase phase;

    MessageType messageType;

    Table table;

    String mes;

    /*******************
     * 4  фазы:
     *      Развитие:
     *          *Положить карту на свое существо
     *          *Положить карту на чужое существо
     *
     *          (Свойство + существо)
     *
     *      Определение К.Б.
     *          *Бросить кубик // Возможно, обойдемся без этого
     *
     *          (Сигнал к бросанию)
     *
     *      Питание:
     *          *Взятие еды из К.Б. (Существо)
     *          *Атака существа (Существо + Свойства, Существо) Пока без свойств
     *          *Защита от атаки (Существо + Свойства) Полагаю свойства в порядке включения?
     *          *Спец действие вне хода (Существо + Свойство) !!!!! Сервер посылает сигнал, что можно такое делать
     *      Вымирание:
     *          Игрок ничего важного не посылает
     *
     ************************/

    public Message(){}
    public Message(Phase phase, MessageType messageType){
        this.phase = phase;
        this.messageType = messageType;
    }
    public Message(MessageType messageType){
        this.messageType = messageType;
    }

    public MessageType getMessageType(){
        return messageType;
    }

    public void setTable(Table table){
        this.table = table;
    }
    public Table getTable() {
        return table;
    }

    public String getMes() {
        return mes;
    }
    public void setMes(String mes) {
        this.mes = mes;
    }
}

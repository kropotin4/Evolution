package server.message;

public class ErrorMessage extends Message{

    /*******************
     * Через этот класс будем пересылать
     * сообщения об ошибках:
     *      0 : Не то отправил (перешли еще раз?)
     *      1 : Не то отправил (Ничего не пересылай)
     *      2 :
     *
     ******************/

    public ErrorMessage(int errorType){

    }

}

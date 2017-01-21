/**
 * Created by jof on 21/01/2017.
 */

import java.util.List;
import javax.smartcardio.*;

public class scanid {
    public static void main(String[] args) {
        try {
            // Affiche la liste des lecteurs
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            System.out.println("Terminals: " + terminals);

            // Utilise le premier lecteur de la liste
            CardTerminal terminal = terminals.get(0);

            // Connexion avec la carte
            Card card = terminal.connect("*");
            System.out.println("card: " + card);
            CardChannel channel = card.getBasicChannel();

            // Envoi la commande Select à l' Applet
            byte[] aid = {(byte)0xA0, 0x00, 0x00, 0x00, 0x62, 0x03, 0x01, 0x0C, 0x06, 0x01};
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
            System.out.println("answer: " + answer.toString());

            // Envoi la commande de test
            answer = channel.transmit(new CommandAPDU(0x00, 0x00, 0x00, 0x00));
            System.out.println("answer: " + answer.toString());
            byte r[] = answer.getData();
            for (int i=0; i<r.length; i++)
                System.out.print((char)r[i]);
            System.out.println();

            // Deconnect la carte
            card.disconnect(false);
        } catch(Exception e) {
            System.out.println("Y a un probleme mon chou, ta carte est pas connectée " + e.toString());
        }
    }
}

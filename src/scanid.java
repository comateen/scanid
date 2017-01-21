/**
 * Created by jof on 21/01/2017.
 */


import java.util.Arrays;
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
            ResponseAPDU reponse = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
            System.out.println("reponse: " + reponse.toString());

            // Envoi la commande de test
            reponse = channel.transmit(new CommandAPDU(0x00, 0x00, 0x00, 0x00));
            System.out.println("reponse: " + reponse.toString());
            byte r[] = reponse.getData();
            for (int i=0; i<r.length; i++)
                System.out.print((char)r[i]);
            System.out.println();

            //Creation d'une commande qui selectionne les fichiers sur la puce
            byte[] IDENTITY_FILE_AID = new byte[] {
                    (byte) 0x3F,// MASTER FILE, Head directory MF "3f00"
                    (byte) 0x00,
                    (byte) 0xDF,// Dedicated File, subdirectory identity DF(ID) "DF01"
                    (byte) 0x01,
                    (byte) 0x40,// Elementary File, the identity file itself EF(ID#RN) "4031"
                    (byte) 0x31
            };

            //Selection des champs à lire
            byte PRENOM = (byte) 0x08;
            byte NOM = (byte) 0x07;
            byte NUMERO_NATIONAL = (byte) 0x06;
            byte DATE_NAISSSANCE = (byte) 0x0C;

            //Creation d'une commande qui va selectionner le fichier d'identité
            CommandAPDU selectFileApdu = new CommandAPDU(0x00, 0xA4, 0x08, 0x0C, IDENTITY_FILE_AID);
            reponse = channel.transmit(selectFileApdu);

            //Lecture complète du fichier d'dentité
            int offset = 0;
            byte[] file = new byte[4096];
            byte[] data;
            do {
                CommandAPDU readBinaryApdu = new CommandAPDU(0x00, 0xB0, offset >> 8, offset & 0xFF, 0xFF);
                ResponseAPDU responseApdu = channel.transmit(readBinaryApdu);
                data = responseApdu.getData();
                System.arraycopy(data, 0, file, offset, data.length);
                offset += data.length;
            } while (0xFF == data.length);

            //les données sont stockées dans le tableau de byte, on les lit, affiche...
            int idx = 0;
            byte length = 0;
            while (idx < file.length) {
                byte tag = file[idx];
                idx++;
                length = file[idx];
                idx++;
                if (NOM == tag) {
                    String nom = new String(Arrays.copyOfRange(file, idx, idx + length));
                    System.out.println("Nom: " + nom);
                }
                if (PRENOM == tag) {
                    String prenom = new String(Arrays.copyOfRange(file, idx, idx + length));
                    System.out.println("Prénom: " + prenom);
                }
                if (NUMERO_NATIONAL == tag) {
                    String numero_national = new String(Arrays.copyOfRange(file, idx, idx + length));
                    System.out.println("Numéro national: " + numero_national);
                }

                if (DATE_NAISSSANCE == tag) {
                    String date_naissance = new String(Arrays.copyOfRange(file, idx, idx + length));
                    System.out.println("date de naissance: " + date_naissance);
                }
                idx += length;
            }

            // Deconnecte la carte
            card.disconnect(false);
        } catch(Exception e) {
            System.out.println("Y a un probleme mon chou " + e.toString());
        }
    }
}

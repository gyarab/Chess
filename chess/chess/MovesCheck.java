/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.ImageView;

/**
 *
 * @author Tomáš
 */
public class MovesCheck {

    ArrayList<ImageView> tiles;

    // vrati vsechny mozne tahy s danou figurkou krome rosady
    public ArrayList checkMoves(int index, ArrayList<ImageView> tilesi) {

        ArrayList<Integer> possibleMoves = new ArrayList();
        tiles = tilesi;

        // rozhodnuti, kterou metodu pouzit podle ID daneho policka
        if (tiles.get(index).getId().contains("bg")) {
            possibleMoves = this.checkBishopBlack(index, tiles);
            possibleMoves = notCheckBlack(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("bk")) {
            possibleMoves = this.checkKingBlack(index, tiles);
            possibleMoves = notCheckBlack(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("bh")) {
            possibleMoves = this.checkKnightBlack(index, tiles);
            possibleMoves = notCheckBlack(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("bp")) {
            possibleMoves = this.checkPinchBlack(index, tiles);
            possibleMoves = notCheckBlack(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("bq")) {
            possibleMoves.addAll(this.checkTowerBlack(index, tiles));
            possibleMoves.addAll(this.checkBishopBlack(index, tiles));
            possibleMoves = notCheckBlack(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("bt")) {
            possibleMoves = this.checkTowerBlack(index, tiles);
            possibleMoves = notCheckBlack(index, tiles, possibleMoves);

        } else if (tiles.get(index).getId().contains("wg")) {
            possibleMoves = this.checkBishopWhite(index, tiles);
            possibleMoves = notCheckWhite(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("wk")) {
            possibleMoves = this.checkKingWhite(index, tiles);
            possibleMoves = notCheckWhite(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("wh")) {
            possibleMoves = this.checkKnightWhite(index, tiles);
            possibleMoves = notCheckWhite(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("wp")) {
            possibleMoves = this.checkPinchWhite(index, tiles);
            possibleMoves = notCheckWhite(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("wq")) {
            possibleMoves.addAll(this.checkTowerWhite(index, tiles));
            possibleMoves.addAll(this.checkBishopWhite(index, tiles));
            possibleMoves = notCheckWhite(index, tiles, possibleMoves);
        } else if (tiles.get(index).getId().contains("wt")) {
            possibleMoves = this.checkTowerWhite(index, tiles);
            possibleMoves = notCheckWhite(index, tiles, possibleMoves);
        }

        return possibleMoves;
    }

    //kontrola, jestli bily kral neni ohrozen
    public ArrayList notCheckWhite(int index, ArrayList<ImageView> tiles, ArrayList<Integer> possibleMoves) {
        int KingPosition = -1;

        Set<Integer> remove = new HashSet<>();
        // postupne se udelaji vsechny tahy, ktere vratily metody checkfigurkabarva a zjistuje se, jestli kral neni v sachu, ty tahy, kdy by byl v sachu, tak se vylouci
        for (int i = 0; i < possibleMoves.size(); i++) {
            ArrayList<ImageView> tiles2 = new ArrayList<>();
            for (int j = 0; j < 64; j++) {
                ImageView a = new ImageView();
                a.setId(tiles.get(j).getId());
                tiles2.add(a);

            }

            if (index != possibleMoves.get(i)) {
                tiles2.get(possibleMoves.get(i)).setId(tiles2.get(index).getId().substring(0, 2) + tiles2.get(possibleMoves.get(i)).getId().substring(2));
                tiles2.get(index).setId("fs" + tiles2.get(index).getId().substring(2));
            }

            for (int j = 0; j < 64; j++) {
                if (tiles2.get(j).getId().contains("wk")) {
                    KingPosition = j;
                    break;
                }
            }
            ArrayList<Integer> check1 = this.checkBishopWhite(KingPosition, tiles2);
            for (int j = 0; j < check1.size(); j++) {
                if (tiles2.get(check1.get(j)).getId().contains("bg") || tiles2.get(check1.get(j)).getId().contains("bq")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check2 = this.checkKingWhite(KingPosition, tiles2);
            for (int j = 0; j < check2.size(); j++) {
                if (tiles2.get(check2.get(j)).getId().contains("bk")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check3 = this.checkKnightWhite(KingPosition, tiles2);
            for (int j = 0; j < check3.size(); j++) {
                if (tiles2.get(check3.get(j)).getId().contains("bh")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check4 = this.checkPinchWhite(KingPosition, tiles2);
            for (int j = 0; j < check4.size(); j++) {
                if (tiles2.get(check4.get(j)).getId().contains("bp")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check5 = this.checkTowerWhite(KingPosition, tiles2);
            for (int j = 0; j < check5.size(); j++) {
                if (tiles2.get(check5.get(j)).getId().contains("bt") || tiles2.get(check5.get(j)).getId().contains("bq")) {
                    remove.add(possibleMoves.get(i));
                }
            }
        }
        for (int i = 0; i < possibleMoves.size(); i++) {
            if (remove.contains(possibleMoves.get(i))) {
                possibleMoves.remove(i);
                i--;
            }
        }

        return possibleMoves;
    }

    // kontrola, jestli cerny kral neni ohrozen
    public ArrayList notCheckBlack(int index, ArrayList<ImageView> tiles, ArrayList<Integer> possibleMoves) {
        int KingPosition = -1;

        Set<Integer> remove = new HashSet<>();
        // postupne se udelaji vsechny tahy, ktere vratily metody checkfigurkabarva a zjistuje se, jestli kral neni v sachu, ty tahy, kdy by byl v sachu, tak se vylouci
        for (int i = 0; i < possibleMoves.size(); i++) {
            ArrayList<ImageView> tiles2 = new ArrayList<>();
            for (int j = 0; j < 64; j++) {
                ImageView a = new ImageView();
                a.setId(tiles.get(j).getId());
                tiles2.add(a);

            }

            if (index != possibleMoves.get(i)) {
                tiles2.get(possibleMoves.get(i)).setId(tiles2.get(index).getId().substring(0, 2) + tiles2.get(possibleMoves.get(i)).getId().substring(2));
                tiles2.get(index).setId("fs" + tiles2.get(index).getId().substring(2));
            }

            for (int j = 0; j < 64; j++) {
                if (tiles2.get(j).getId().contains("bk")) {
                    KingPosition = j;
                    break;
                }
            }

            ArrayList<Integer> check1 = this.checkBishopBlack(KingPosition, tiles2);
            for (int j = 0; j < check1.size(); j++) {
                if (tiles2.get(check1.get(j)).getId().contains("wg") || tiles2.get(check1.get(j)).getId().contains("wq")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check2 = this.checkKingBlack(KingPosition, tiles2);
            for (int j = 0; j < check2.size(); j++) {
                if (tiles2.get(check2.get(j)).getId().contains("wk")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check3 = this.checkKnightBlack(KingPosition, tiles2);
            for (int j = 0; j < check3.size(); j++) {
                if (tiles2.get(check3.get(j)).getId().contains("wh")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check4 = this.checkPinchBlack(KingPosition, tiles2);
            for (int j = 0; j < check4.size(); j++) {
                if (tiles2.get(check4.get(j)).getId().contains("wp")) {
                    remove.add(possibleMoves.get(i));
                }
            }
            ArrayList<Integer> check5 = this.checkTowerBlack(KingPosition, tiles2);
            for (int j = 0; j < check5.size(); j++) {
                if (tiles2.get(check5.get(j)).getId().contains("wt") || tiles2.get(check5.get(j)).getId().contains("wq")) {
                    remove.add(possibleMoves.get(i));
                }
            }
        }

        for (int i = 0; i < possibleMoves.size(); i++) {
            if (remove.contains(possibleMoves.get(i))) {
                possibleMoves.remove(i);
                i--;
            }
        }
        return possibleMoves;
    }

    // vrati tahy, ktere pesec muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkPinchBlack(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();
        if (tiles.get(index + 8).getId().contains("fs")) {
            possibleMoves.add(index + 8);
            if (index < 16 && tiles.get(index + 16).getId().contains("fs")) {
                possibleMoves.add(index + 16);
            }

        }
        if (index + 7 < 64 && tiles.get(index + 7).getId().contains("w") && (index) % 8 != 0) {
            possibleMoves.add(index + 7);
        }
        if (index + 9 < 64 && tiles.get(index + 9).getId().contains("w") && (index) % 8 != 7) {
            possibleMoves.add(index + 9);
        }
        return possibleMoves;
    }

    // vrati tahy, ktere pesec muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkPinchWhite(int index, ArrayList<ImageView> tiles) {

        ArrayList<Integer> possibleMoves = new ArrayList();
        if (tiles.get(index - 8).getId().contains("fs")) {
            possibleMoves.add(index - 8);
            if ((index < 56 && index > 47) && tiles.get(index - 16).getId().contains("fs")) {
                possibleMoves.add(index - 16);
            }

        }
        if (index - 7 >= 0 && tiles.get(index - 7).getId().contains("b") && (index) % 8 != 7) {
            possibleMoves.add(index - 7);
        }
        if (index - 9 >= 0 && tiles.get(index - 9).getId().contains("b") && (index) % 8 != 0) {
            possibleMoves.add(index - 9);
        }

        return possibleMoves;

    }

    // vrati tahy, ktere vez muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkTowerBlack(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();
        for (int i = 1; i < 8; i++) {

            if (index - 8 * i < 0 || tiles.get(index - 8 * (i - 1)).getId().contains("w") || tiles.get(index - 8 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index - 8 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index - 1 * i < 0 || (index - 1 * (i - 1)) % 8 == 0 || tiles.get(index - 1 * (i - 1)).getId().contains("w") || tiles.get(index - 1 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index - 1 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index + 8 * i > 63 || tiles.get(index + 8 * (i - 1)).getId().contains("w") || tiles.get(index + 8 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index + 8 * i);

        }

        for (int i = 1; i < 8; i++) {

            if (index + 1 * i > 63 || (index + 1 * (i - 1)) % 8 == 7 || tiles.get(index + 1 * (i - 1)).getId().contains("w") || tiles.get(index + 1 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index + 1 * i);
        }

        return possibleMoves;
    }

    // vrati tahy, ktere vez muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkTowerWhite(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();
        for (int i = 1; i < 8; i++) {

            if (index - 8 * i < 0 || tiles.get(index - 8 * (i - 1)).getId().contains("b") || tiles.get(index - 8 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index - 8 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index - 1 * i < 0 || (index - 1 * (i - 1)) % 8 == 0 || tiles.get(index - 1 * (i - 1)).getId().contains("b") || tiles.get(index - 1 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index - 1 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index + 8 * i > 63 || tiles.get(index + 8 * (i - 1)).getId().contains("b") || tiles.get(index + 8 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index + 8 * i);

        }

        for (int i = 1; i < 8; i++) {

            if (index + 1 * i > 63 || (index + 1 * (i - 1)) % 8 == 7 || tiles.get(index + 1 * (i - 1)).getId().contains("b") || tiles.get(index + 1 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index + 1 * i);
        }

        return possibleMoves;
    }

    // vrati tahy, ktere jezdec muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkKnightBlack(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();
        if (index - 17 >= 0 && index % 8 != 0 && !tiles.get(index - 17).getId().contains("b")) {
            possibleMoves.add(index - 17);
        }
        if (index - 15 >= 0 && index % 8 != 7 && !tiles.get(index - 15).getId().contains("b")) {
            possibleMoves.add(index - 15);
        }
        if (index - 10 >= 0 && index % 8 > 1 && !tiles.get(index - 10).getId().contains("b")) {
            possibleMoves.add(index - 10);
        }
        if (index - 6 >= 0 && index % 8 < 6 && !tiles.get(index - 6).getId().contains("b")) {
            possibleMoves.add(index - 6);
        }
        if (index + 6 < 64 && index % 8 > 1 && !tiles.get(index + 6).getId().contains("b")) {
            possibleMoves.add(index + 6);
        }
        if (index + 10 < 64 && index % 8 < 6 && !tiles.get(index + 10).getId().contains("b")) {
            possibleMoves.add(index + 10);
        }
        if (index + 15 < 64 && index % 8 != 0 && !tiles.get(index + 15).getId().contains("b")) {
            possibleMoves.add(index + 15);
        }
        if (index + 17 < 64 && index % 8 != 7 && !tiles.get(index + 17).getId().contains("b")) {
            possibleMoves.add(index + 17);
        }
        return possibleMoves;
    }

    // vrati tahy, ktere jezdec muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkKnightWhite(int index, ArrayList<ImageView> tiles) {

        ArrayList<Integer> possibleMoves = new ArrayList();
        if (index - 17 >= 0 && index % 8 != 0 && !tiles.get(index - 17).getId().contains("w")) {
            possibleMoves.add(index - 17);
        }
        if (index - 15 >= 0 && index % 8 != 7 && !tiles.get(index - 15).getId().contains("w")) {
            possibleMoves.add(index - 15);
        }
        if (index - 10 >= 0 && index % 8 > 1 && !tiles.get(index - 10).getId().contains("w")) {
            possibleMoves.add(index - 10);
        }
        if (index - 6 >= 0 && index % 8 < 6 && !tiles.get(index - 6).getId().contains("w")) {
            possibleMoves.add(index - 6);

        }
        if (index + 6 < 64 && index % 8 > 1 && !tiles.get(index + 6).getId().contains("w")) {
            possibleMoves.add(index + 6);
        }
        if (index + 10 < 64 && index % 8 < 6 && !tiles.get(index + 10).getId().contains("w")) {
            possibleMoves.add(index + 10);
        }
        if (index + 15 < 64 && index % 8 != 0 && !tiles.get(index + 15).getId().contains("w")) {
            possibleMoves.add(index + 15);
        }
        if (index + 17 < 64 && index % 8 != 7 && !tiles.get(index + 17).getId().contains("w")) {
            possibleMoves.add(index + 17);
        }
        return possibleMoves;
    }

    // vrati tahy, ktere strelec muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkBishopBlack(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();
        for (int i = 1; i < 8; i++) {

            if (index - 9 * i < 0 || (index - 9 * (i - 1)) % 8 == 0 || tiles.get(index - 9 * (i - 1)).getId().contains("w") || tiles.get(index - 9 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index - 9 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index - 7 * i < 0 || (index - 7 * (i - 1)) % 8 == 7 || tiles.get(index - 7 * (i - 1)).getId().contains("w") || tiles.get(index - 7 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index - 7 * i);
        }
        for (int i = 1; i < 8; i++) {

            if (index + 7 * i > 63 || (index + 7 * (i - 1)) % 8 == 0 || tiles.get(index + 7 * (i - 1)).getId().contains("w") || tiles.get(index + 7 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index + 7 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index + 9 * i > 63 || (index + 9 * (i - 1)) % 8 == 7 || tiles.get(index + 9 * (i - 1)).getId().contains("w") || tiles.get(index + 9 * i).getId().contains("b")) {
                break;
            }
            possibleMoves.add(index + 9 * i);
        }
        return possibleMoves;
    }

    // vrati tahy, ktere strelec muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkBishopWhite(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();
        for (int i = 1; i < 8; i++) {
            if (index - 9 * i < 0 || (index - 9 * (i - 1)) % 8 == 0 || tiles.get(index - 9 * (i - 1)).getId().contains("b") || tiles.get(index - 9 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index - 9 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index - 7 * i < 0 || (index - 7 * (i - 1)) % 8 == 7 || tiles.get(index - 7 * (i - 1)).getId().contains("b") || tiles.get(index - 7 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index - 7 * i);
        }

        for (int i = 1; i < 8; i++) {
            if (index + 7 * i > 63 || (index + 7 * (i - 1)) % 8 == 0 || tiles.get(index + 7 * (i - 1)).getId().contains("b") || tiles.get(index + 7 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index + 7 * i);
        }

        for (int i = 1; i < 8; i++) {

            if (index + 9 * i > 63 || (index + 9 * (i - 1)) % 8 == 7 || tiles.get(index + 9 * (i - 1)).getId().contains("b") || tiles.get(index + 9 * i).getId().contains("w")) {
                break;
            }
            possibleMoves.add(index + 9 * i);
        }

        return possibleMoves;
    }

    // vrati tahy, ktere kral muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkKingBlack(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();

        if (index % 8 != 0 && !tiles.get(index - 1).getId().contains("b")) {
            possibleMoves.add(index - 1);
        }
        if (index % 8 != 7 && !tiles.get(index + 1).getId().contains("b")) {
            possibleMoves.add(index + 1);
        }
        if (index > 7 && index % 8 != 0 && !tiles.get(index - 9).getId().contains("b")) {
            possibleMoves.add(index - 9);
        }
        if (index - 8 >= 0 && !tiles.get(index - 8).getId().contains("b")) {
            possibleMoves.add(index - 8);
        }
        if (index > 7 && index % 8 != 7 && !tiles.get(index - 7).getId().contains("b")) {
            possibleMoves.add(index - 7);
        }
        if (index % 8 != 0 && index + 7 < 64 && !tiles.get(index + 7).getId().contains("b")) {
            possibleMoves.add(index + 7);
        }
        if (index + 8 < 64 && !tiles.get(index + 8).getId().contains("b")) {
            possibleMoves.add(index + 8);
        }
        if (index + 9 < 64 && index % 8 != 7 && !tiles.get(index + 9).getId().contains("b")) {
            possibleMoves.add(index + 9);
        }
        return possibleMoves;
    }

    // vrati tahy, ktere kral muze provest, ale i takove, kterymi by byl ohrozen vlastni kral
    private ArrayList checkKingWhite(int index, ArrayList<ImageView> tiles) {
        ArrayList<Integer> possibleMoves = new ArrayList();
        if (index % 8 != 0 && !tiles.get(index - 1).getId().contains("w")) {
            possibleMoves.add(index - 1);
        }
        if (index % 8 != 7 && !tiles.get(index + 1).getId().contains("w")) {
            possibleMoves.add(index + 1);
        }
        if (index > 7 && index % 8 != 0 && !tiles.get(index - 9).getId().contains("w")) {
            possibleMoves.add(index - 9);
        }
        if (index - 8 >= 0 && !tiles.get(index - 8).getId().contains("w")) {
            possibleMoves.add(index - 8);
        }
        if (index > 7 && index % 8 != 7 && !tiles.get(index - 7).getId().contains("w")) {
            possibleMoves.add(index - 7);
        }
        if (index % 8 != 0 && index + 7 < 64 && !tiles.get(index + 7).getId().contains("w")) {
            possibleMoves.add(index + 7);
        }
        if (index + 8 < 64 && !tiles.get(index + 8).getId().contains("w")) {
            possibleMoves.add(index + 8);
        }
        if (index + 9 < 64 && index % 8 != 7 && !tiles.get(index + 9).getId().contains("w")) {
            possibleMoves.add(index + 9);
        }

        return possibleMoves;
    }

}

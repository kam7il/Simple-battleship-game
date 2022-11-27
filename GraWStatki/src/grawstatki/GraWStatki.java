/*
czteromasztowiec x 1
trójmasztowce x 2
dwumasztowce x 3
jednomasztowce x 4
 */

 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grawstatki;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.abs;
//import java.io.PrintWriter;
import java.util.Scanner;

class obecne {
    public static String obecnyLogin = "Brak użytkownika";
}

class menu {

    int wybor;
    Scanner pisanieInt = new Scanner(System.in);

    int logowanie() throws FileNotFoundException {
        System.out.println("1 - Zaloguj");
        System.out.println("2 - Utwórz Konto");

        System.out.println("Proszę wybrać opcję od 1-2");
        wybor = 0;
        try {
            wybor = pisanieInt.nextInt();
        } catch (Exception e) {
            System.out.println("Nie podano cyfry");
            //wybor = 3;
            return 3;//nie wiem czemu nie wraca mi do 3 jak to robi sie w przypadku spełnienia warunku w linii 48
            //throw e;
        }
        if (wybor != 2 && wybor != 1) {
            System.out.println("Proszę wybrać 1 lub 2");
            return 3;
        } else {
            if (wybor == 1) {
                return 1; // wczytywanie uzytkownika 
            } else {
                return 2; // tworzenie uzytkownika 
            }
        }
    }

    int menu() {
        System.out.println("Witaj " + obecne.obecnyLogin);
        System.out.println("1 - Nowa Gra");
        System.out.println("2 - Wczytaj Gre (nie zaimplementowane)");
        System.out.println("3 - Powtorki (nie zaimplementowane)");
        System.out.println("4 - Wylogowanie");

        System.out.println("Proszę wybrać opcję od 1-4");
        wybor = pisanieInt.nextInt();

        if (wybor != 2 && wybor != 1 && wybor != 3 && wybor != 4) {
            System.out.print("Proszę wybrać od 1 do 4");
        } else {
            switch (wybor) {
                case 1:
                    System.out.println("Wybrano nowa gra");
                    return 5; //wybranie nowej gry
                //break;
                case 2:
                    System.out.println("Wybrano wczytaj gre");
                    break;
                case 3:
                    System.out.println("Wybrano powtorki");
                    break;
                case 4:
                    obecne.obecnyLogin = "Brak użytkownika";
                    return 3; //menu logowania
                //System.out.println("Wybrano wylogowanie");
                //break;
                default:
                    break;
            }
        }
        return 0;
    }
}

class uzytkownik {

    String login;
    String haslo;
    Scanner pisanie = new Scanner(System.in);

    int dodawanieUzytkownika() throws IOException {
        String temp;
        Scanner pisanieLogin = new Scanner(System.in);
        System.out.print("Podaj login: ");
        login = pisanieLogin.next();

        try (Scanner read = new Scanner(new File("users.txt"))) { //sprawdza czy już taki login jest
            read.useDelimiter(";");

            while (read.hasNext()) {
                //System.out.print("test");
                temp = read.next();
                if (temp.equals(login)) {
                    System.out.println("Użytkownik o takim loginie już istnieje, proszę wybrać inny login");
                    return 2;
                } else {
                    read.nextLine();
                }
            }
            read.close();
        }

        try (FileWriter zapis = new FileWriter("users.txt", true)) {//dopisuje do pliku jak nie ma takie uzytkownika
            //System.out.print("Podaj swoj login: ");
            //this.login = pisanie.next();
            zapis.write(login + ";");

            System.out.print("Podaj swoje haslo: ");
            this.haslo = pisanie.next();
            zapis.write(haslo + ";" + "\n");

            zapis.close();
        }
        return 3; //menu logowania 
    }

    int wczytywanieUzytkownika() throws FileNotFoundException {
        String temp;
        //String haslo;
        String hasloPodane;
        Scanner pisanieLogin = new Scanner(System.in);

        System.out.print("Podaj login: ");
        String tempLogin = pisanieLogin.next();

        try (Scanner read = new Scanner(new File("users.txt"))) {
            read.useDelimiter(";");

            while (read.hasNext()) {
                //System.out.print("test");
                temp = read.next();
                if (temp.equals(tempLogin)) {
                    System.out.print("Witaj " + temp + " proszę, ");
                    haslo = read.next();
                    System.out.print("podaj haslo: ");
                    hasloPodane = pisanie.next();
                    System.out.println("");
                    while (!haslo.equals(hasloPodane)) {
                        System.out.print("Złe hasło, wpisz \"Koniec!\" lub spróbuj ponownie: ");
                        hasloPodane = pisanie.next();
                        if (hasloPodane.equals("Koniec!")) {
                            break;
                        }
                    }
                    if (hasloPodane.equals("Koniec!")) {
                        System.out.println("Anulowano logowanie");

                        return 3; //menu logowania
                    } else {
                        obecne.obecnyLogin = tempLogin;
                        System.out.println("Zalogowano");
                        return 4; //menu gry
                    }
                } else {
                    read.nextLine();
                }
            }
            System.out.println("Nie ma takiego użytkownika");
            read.close();
            return 3;
        }
    }
}

class Gra {

    char[][] plansza = new char[12][12]; //plansza na moje statki
    char[][] plansza1 = new char[12][12]; //plansza na strzały

    int wspolXstart;
    int wspolYstart;
    int wspolXkoniec;
    int wspolYkoniec;
    int strzelanieX;
    int strzelanieY;
    Scanner pisanie = new Scanner(System.in);

    void planszaPustaTworzenie() {//tworzenie pustej planszy na moje statki
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                plansza[i][j] = 'O';
            }
        }
    }

    void planszaWyswietlanie() {//wyświetlanie planszy do statków
        int licznik = 0;
        int licznik1 = 0;
        System.out.println("  Legenda: @ - trafiony, X - pudło");
        System.out.println("   A B C D E F G H I J");
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        System.out.println("--------------------------");
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                if (j == 1) {
                    if (i == 10) {
                        System.out.print(++licznik + "|");
                    } else {
                        System.out.print(++licznik + " |");
                    }
                    System.out.print(plansza[i][j] + " ");
                } else if (j == 10) {
                    System.out.print(plansza[i][j] + " ");
                    if (i == 10) {
                        System.out.print("|" + ++licznik1);
                    } else {
                        System.out.print("| " + ++licznik1);
                    }
                } else {
                    System.out.print(plansza[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------");
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        System.out.println("   A B C D E F G H I J");
    }

    void dodawanieStatkow() {//dodawanie wszystkich statków
        planszaWyswietlanie();

        //wpisywanie czteromasztowca
        int straznik = -9;
        while (straznik == -9) {
            System.out.println("Podaj X pierwszej współrzędnej czteromasztowca");
            wspolXstart = pisanie.nextInt();
            if (wspolXstart < 1 || wspolXstart > 10) {
                System.out.println("Niepoprawna współrzędna X pierwszego punktu");
            } else {
                straznik = -8;
            }
        }

        while (straznik == -8) {
            System.out.println("Podaj Y pierwszej współrzędnej czteromasztowca");
            wspolYstart = pisanie.nextInt();
            if (wspolYstart < 1 || wspolYstart > 10) {
                System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
            } else {
                straznik = -7;
            }
        }

        while (straznik == -7) {
            System.out.println("Podaj X drugiej współrzędnej czteromasztowca");
            wspolXkoniec = pisanie.nextInt();
            System.out.println("Podaj Y drugiej współrzędnej czteromasztowca");
            wspolYkoniec = pisanie.nextInt();
            straznik = -6;

            if (wspolXkoniec < 1 || wspolXkoniec > 10) {
                System.out.println("Niepoprawna współrzędna X drugiego punktu");
                straznik = -7;
            }
            if (wspolYkoniec < 1 || wspolYkoniec > 10) {
                System.out.println("Niepoprawna współrzędna Y drugiego punktu");
                straznik = -7;
            }
            if (abs(wspolXstart - wspolXkoniec) != 3 && abs(wspolYstart - wspolYkoniec) != 3) {
                System.out.println("Statek ma być czteromasztowcem");
                straznik = -7;
            }
            if (wspolXstart != wspolXkoniec && wspolYstart != wspolYkoniec) {
                System.out.println("Statek nie może być postawiony na ukos");
                straznik = -7;
            }
        }

        //dodawanie czteromasztowca do tablicy
        if (wspolXstart == wspolXkoniec) {
            if (wspolYstart > wspolYkoniec) {
                for (int i = 0; i < 4; i++) {
                    plansza[wspolYstart--][wspolXstart] = '#';
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    plansza[wspolYstart++][wspolXstart] = '#';
                }
            }
        } else {
            if (wspolXstart > wspolXkoniec) {
                for (int i = 0; i < 4; i++) {
                    plansza[wspolYstart][wspolXstart--] = '#';
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    plansza[wspolYstart][wspolXstart++] = '#';
                }
            }
        }

        planszaWyswietlanie();

        //wpisywanie trzymasztowca 1
        int straznik1 = -9;
        int straznik2 = 0;
        while (straznik2 != 3) {
            straznik1 = -9;
            straznik2 = 0;

            while (straznik1 == -9) {
                System.out.println("Podaj X pierwszej współrzędnej trzymasztowca pierwszego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna X pierwszego punktu");
                } else {
                    straznik1 = -8;
                }
            }

            while (straznik1 == -8) {
                System.out.println("Podaj Y pierwszej współrzędnej trzymasztowca pierwszego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
                } else {
                    straznik1 = -7;
                }
            }

            while (straznik1 == -7) {
                System.out.println("Podaj X drugiej współrzędnej trzymasztowca pierwszego");
                wspolXkoniec = pisanie.nextInt();
                System.out.println("Podaj Y drugiej współrzędnej trzymasztowca pierwszego");
                wspolYkoniec = pisanie.nextInt();
                straznik1 = -6;

                if (wspolXkoniec < 1 || wspolXkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna X drugiego punktu");
                    straznik1 = -7;
                }
                if (wspolYkoniec < 1 || wspolYkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna Y drugiego punktu");
                    straznik1 = -7;
                }
                if (abs(wspolXstart - wspolXkoniec) != 2 && abs(wspolYstart - wspolYkoniec) != 2) {
                    System.out.println("Statek ma być trzymasztowcem");
                    straznik1 = -7;
                }
                if (wspolXstart != wspolXkoniec && wspolYstart != wspolYkoniec) {
                    System.out.println("Statek nie może być postawiony na ukos");
                    straznik1 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1
            if (wspolXstart == wspolXkoniec) { //jeśli statek ma dlugość daną poprzez Y
                if (wspolYstart > wspolYkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart--;
                        if (i != 4) {
                            straznik2++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik2 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart + 3;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart++;
                        if (i != 4) {
                            straznik2++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik2 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart - 3;
                        }
                    }
                }
            } else {//jeśli statek ma dlugość daną poprzez X
                if (wspolXstart > wspolXkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolXstart--;
                        if (i != 4) {
                            straznik2++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik2 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart + 3;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        wspolXstart++; // tu byl problem co do tego na górze #1
                        if (i != 4) {
                            straznik2++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik2 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart - 3;
                        }
                    }
                }
            }
        }

        //dodawanie trzymasztowca 1 do tablicy
        if (wspolXstart == wspolXkoniec) {
            if (wspolYstart > wspolYkoniec) {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart--][wspolXstart] = '#';
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart++][wspolXstart] = '#';
                }
            }
        } else {
            if (wspolXstart > wspolXkoniec) {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart][wspolXstart--] = '#';
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart][wspolXstart++] = '#';
                }
            }
        }

        planszaWyswietlanie();
        //wpisywanie trzymasztowca 2
        int straznik3 = -9;
        int straznik4 = 0;
        while (straznik4 != 3) {
            straznik3 = -9;
            straznik4 = 0;

            while (straznik3 == -9) {
                System.out.println("Podaj X pierwszej współrzędnej trzymasztowca drugiego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna X drugiego punktu");
                } else {
                    straznik3 = -8;
                }
            }

            while (straznik3 == -8) {
                System.out.println("Podaj Y pierwszej współrzędnej trzymasztowca drugiego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna Y drugiego punktu");
                } else {
                    straznik3 = -7;
                }
            }

            while (straznik3 == -7) {
                System.out.println("Podaj X drugiej współrzędnej trzymasztowca drugiego");
                wspolXkoniec = pisanie.nextInt();
                System.out.println("Podaj Y drugiej współrzędnej trzymasztowca drugiego");
                wspolYkoniec = pisanie.nextInt();
                straznik3 = -6;

                if (wspolXkoniec < 1 || wspolXkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna X drugiego punktu");
                    straznik3 = -7;
                }
                if (wspolYkoniec < 1 || wspolYkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna Y drugiego punktu");
                    straznik3 = -7;
                }
                if (abs(wspolXstart - wspolXkoniec) != 2 && abs(wspolYstart - wspolYkoniec) != 2) {
                    System.out.println("Statek ma być trzymasztowcem");
                    straznik3 = -7;
                }
                if (wspolXstart != wspolXkoniec && wspolYstart != wspolYkoniec) {
                    System.out.println("Statek nie może być postawiony na ukos");
                    straznik3 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2
            if (wspolXstart == wspolXkoniec) { //jeśli statek ma dlugość daną poprzez Y
                if (wspolYstart > wspolYkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart--;
                        if (i != 4) {
                            straznik4++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik4 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart + 3;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart++;
                        if (i != 4) {
                            straznik4++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik4 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart - 3;
                        }
                    }
                }
            } else {//jeśli statek ma dlugość daną poprzez X
                if (wspolXstart > wspolXkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolXstart--;
                        if (i != 4) {
                            straznik4++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik4 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart + 3;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 3; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        wspolXstart++; // tu byl problem co do tego na górze #1
                        if (i != 4) {
                            straznik4++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik4 == 3) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart - 3;
                        }
                    }
                }
            }
        }

        //dodawanie trzymasztowca 2 do tablicy
        if (wspolXstart == wspolXkoniec) {
            if (wspolYstart > wspolYkoniec) {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart--][wspolXstart] = '#';
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart++][wspolXstart] = '#';
                }
            }
        } else {
            if (wspolXstart > wspolXkoniec) {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart][wspolXstart--] = '#';
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    plansza[wspolYstart][wspolXstart++] = '#';
                }
            }
        }

        planszaWyswietlanie();

        //wpisywanie dwumasztowca 1
        int straznik5 = -9;
        int straznik6 = 0;
        while (straznik6 != 2) {
            straznik5 = -9;
            straznik6 = 0;

            while (straznik5 == -9) {
                System.out.println("Podaj X pierwszej współrzędnej dwumasztowca pierwszego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna X pierwszego punktu");
                } else {
                    straznik5 = -8;
                }
            }

            while (straznik5 == -8) {
                System.out.println("Podaj Y pierwszej współrzędnej dwumasztowca pierwszego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
                } else {
                    straznik5 = -7;
                }
            }

            while (straznik5 == -7) {
                System.out.println("Podaj X drugiej współrzędnej dwumasztowca pierwszego");
                wspolXkoniec = pisanie.nextInt();
                System.out.println("Podaj Y drugiej współrzędnej dwumasztowca pierwszego");
                wspolYkoniec = pisanie.nextInt();
                straznik5 = -6;

                if (wspolXkoniec < 1 || wspolXkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna X pierwszego punktu");
                    straznik5 = -7;
                }
                if (wspolYkoniec < 1 || wspolYkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
                    straznik5 = -7;
                }
                if (abs(wspolXstart - wspolXkoniec) != 1 && abs(wspolYstart - wspolYkoniec) != 1) {
                    System.out.println("Statek ma być dwumasztowcem");
                    straznik5 = -7;
                }
                if (wspolXstart != wspolXkoniec && wspolYstart != wspolYkoniec) {
                    System.out.println("Statek nie może być postawiony na ukos");
                    straznik5 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2 i 2_1
            if (wspolXstart == wspolXkoniec) { //jeśli statek ma dlugość daną poprzez Y
                if (wspolYstart > wspolYkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart--;
                        if (i != 4) {
                            straznik6++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik6 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart + 2;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart++;
                        if (i != 4) {
                            straznik6++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik6 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart - 2;
                        }
                    }
                }
            } else {//jeśli statek ma dlugość daną poprzez X
                if (wspolXstart > wspolXkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolXstart--;
                        if (i != 4) {
                            straznik6++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik6 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart + 2;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        wspolXstart++; // tu byl problem co do tego na górze #1
                        if (i != 4) {
                            straznik6++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik6 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart - 2;
                        }
                    }
                }
            }
        }

        //dodawanie dwumasztowca 1 do tablicy
        if (wspolXstart == wspolXkoniec) {
            if (wspolYstart > wspolYkoniec) {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart--][wspolXstart] = '#';
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart++][wspolXstart] = '#';
                }
            }
        } else {
            if (wspolXstart > wspolXkoniec) {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart][wspolXstart--] = '#';
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart][wspolXstart++] = '#';
                }
            }
        }

        planszaWyswietlanie();

        //wpisywanie dwumasztowca 2
        int straznik7 = -9;
        int straznik8 = 0;
        while (straznik8 != 2) {
            straznik7 = -9;
            straznik8 = 0;

            while (straznik7 == -9) {
                System.out.println("Podaj X pierwszej współrzędnej dwumasztowca drugiego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna X pierwszego punktu");
                } else {
                    straznik7 = -8;
                }
            }

            while (straznik7 == -8) {
                System.out.println("Podaj Y pierwszej współrzędnej dwumasztowca drugiego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
                } else {
                    straznik7 = -7;
                }
            }

            while (straznik7 == -7) {
                System.out.println("Podaj X drugiej współrzędnej dwumasztowca drugiego");
                wspolXkoniec = pisanie.nextInt();
                System.out.println("Podaj Y drugiej współrzędnej dwumasztowca drugiego");
                wspolYkoniec = pisanie.nextInt();
                straznik7 = -6;

                if (wspolXkoniec < 1 || wspolXkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna X pierwszego punktu");
                    straznik7 = -7;
                }
                if (wspolYkoniec < 1 || wspolYkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
                    straznik7 = -7;
                }
                if (abs(wspolXstart - wspolXkoniec) != 1 && abs(wspolYstart - wspolYkoniec) != 1) {
                    System.out.println("Statek ma być dwumasztowcem");
                    straznik7 = -7;
                }
                if (wspolXstart != wspolXkoniec && wspolYstart != wspolYkoniec) {
                    System.out.println("Statek nie może być postawiony na ukos");
                    straznik7 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2 i 2_1 i 2_2
            if (wspolXstart == wspolXkoniec) { //jeśli statek ma dlugość daną poprzez Y
                if (wspolYstart > wspolYkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart--;
                        if (i != 4) {
                            straznik8++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik8 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart + 2;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart++;
                        if (i != 4) {
                            straznik8++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik8 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart - 2;
                        }
                    }
                }
            } else {//jeśli statek ma dlugość daną poprzez X
                if (wspolXstart > wspolXkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolXstart--;
                        if (i != 4) {
                            straznik8++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik8 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart + 2;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        wspolXstart++; // tu byl problem co do tego na górze #1
                        if (i != 4) {
                            straznik8++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik8 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart - 2;
                        }
                    }
                }
            }
        }

        //dodawanie dwumasztowca 2 do tablicy
        if (wspolXstart == wspolXkoniec) {
            if (wspolYstart > wspolYkoniec) {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart--][wspolXstart] = '#';
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart++][wspolXstart] = '#';
                }
            }
        } else {
            if (wspolXstart > wspolXkoniec) {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart][wspolXstart--] = '#';
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart][wspolXstart++] = '#';
                }
            }
        }

        planszaWyswietlanie();

        //wpisywanie dwumasztowca 3
        int straznik9 = -9;
        int straznik10 = 0;
        while (straznik10 != 2) {
            straznik9 = -9;
            straznik10 = 0;

            while (straznik9 == -9) {
                System.out.println("Podaj X pierwszej współrzędnej dwumasztowca trzeciego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna X pierwszego punktu");
                } else {
                    straznik9 = -8;
                }
            }

            while (straznik9 == -8) {
                System.out.println("Podaj Y pierwszej współrzędnej dwumasztowca trzeciego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
                } else {
                    straznik9 = -7;
                }
            }

            while (straznik9 == -7) {
                System.out.println("Podaj X drugiej współrzędnej dwumasztowca trzeciego");
                wspolXkoniec = pisanie.nextInt();
                System.out.println("Podaj Y drugiej współrzędnej dwumasztowca trzeciego");
                wspolYkoniec = pisanie.nextInt();
                straznik9 = -6;

                if (wspolXkoniec < 1 || wspolXkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna X pierwszego punktu");
                    straznik9 = -7;
                }
                if (wspolYkoniec < 1 || wspolYkoniec > 10) {
                    System.out.println("Niepoprawna współrzędna Y pierwszego punktu");
                    straznik9 = -7;
                }
                if (abs(wspolXstart - wspolXkoniec) != 1 && abs(wspolYstart - wspolYkoniec) != 1) {
                    System.out.println("Statek ma być dwumasztowcem");
                    straznik9 = -7;
                }
                if (wspolXstart != wspolXkoniec && wspolYstart != wspolYkoniec) {
                    System.out.println("Statek nie może być postawiony na ukos");
                    straznik9 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2 i 2_1 i 2_2 i 2_3
            if (wspolXstart == wspolXkoniec) { //jeśli statek ma dlugość daną poprzez Y
                if (wspolYstart > wspolYkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart--;
                        if (i != 4) {
                            straznik10++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik10 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart + 2;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolYstart++;
                        if (i != 4) {
                            straznik10++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik10 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolYstart = wspolYstart - 2;
                        }
                    }
                }
            } else {//jeśli statek ma dlugość daną poprzez X
                if (wspolXstart > wspolXkoniec) {//startujemy pisanie od lewej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }
                        wspolXstart--;
                        if (i != 4) {
                            straznik10++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik10 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart + 2;
                        }
                    }
                } else {//startujemy pisanie od prawej
                    for (int i = 0; i < 2; i++) {
                        if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0) {
                            if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart][wspolXstart - 1]) == '#') { //tu byl problem #1
                                i = 4;
                            }
                        }

                        if ((plansza[wspolYstart][wspolXstart]) == '#') {
                            i = 4;
                        }

                        if (wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolXstart - 1 > 0) {
                            if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                                i = 4;
                            }
                        }

                        if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                            if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                                i = 4;
                            }
                        }

                        wspolXstart++; // tu byl problem co do tego na górze #1
                        if (i != 4) {
                            straznik10++;
                        }

                        if (i == 4) {
                            System.out.println("Statek jest za blisko innego");
                        }

                        if (straznik10 == 2) { // powrót współrzednych do poprzedniego stanu, aby do tablicy poprawnie się wstawiały
                            wspolXstart = wspolXstart - 2;
                        }
                    }
                }
            }
        }

        //dodawanie dwumasztowca 3 do tablicy
        if (wspolXstart == wspolXkoniec) {
            if (wspolYstart > wspolYkoniec) {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart--][wspolXstart] = '#';
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart++][wspolXstart] = '#';
                }
            }
        } else {
            if (wspolXstart > wspolXkoniec) {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart][wspolXstart--] = '#';
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    plansza[wspolYstart][wspolXstart++] = '#';
                }
            }
        }

        planszaWyswietlanie();

        //wpisywanie jednomasztowca 1
        int straznik11 = -9;
        int straznik12 = 0;
        while (straznik12 != 2) {
            straznik11 = -9;
            straznik12 = 0;

            while (straznik11 == -9) {
                System.out.println("Podaj współrzędną X jednomasztowca pierwszego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu X");
                } else {
                    straznik11 = -8;
                }
            }

            while (straznik11 == -8) {
                System.out.println("Podaj współrzędną Y jednomasztowca pierwszego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu Y");
                } else {
                    straznik11 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2 i 2_1 i 2_2 i 2_3 i 1_1
            for (int i = 0; i < 1; i++) {
                if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if ((plansza[wspolYstart][wspolXstart]) == '#') {
                    i = 4;
                }

                if (wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (i == 4) {
                    System.out.println("Statek jest za blisko innego");
                } else {
                    straznik12 = 2;
                }
            }
        }

        //dodawanie jednomasztowca 1 do tablicy
        plansza[wspolYstart][wspolXstart] = '#';

        planszaWyswietlanie();

        //wpisywanie jednomasztowca 2
        int straznik13 = -9;
        int straznik14 = 0;
        while (straznik14 != 2) {
            straznik13 = -9;
            straznik14 = 0;

            while (straznik13 == -9) {
                System.out.println("Podaj współrzędną X jednomasztowca drugiego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu X");
                } else {
                    straznik13 = -8;
                }
            }

            while (straznik13 == -8) {
                System.out.println("Podaj współrzędną Y jednomasztowca drugiego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu Y");
                } else {
                    straznik13 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2 i 2_1 i 2_2 i 2_3 i 1_1 i 1_2
            for (int i = 0; i < 1; i++) {
                if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if ((plansza[wspolYstart][wspolXstart]) == '#') {
                    i = 4;
                }

                if (wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (i == 4) {
                    System.out.println("Statek jest za blisko innego");
                } else {
                    straznik14 = 2;
                }
            }
        }

        //dodawanie jednomasztowca 2 do tablicy
        plansza[wspolYstart][wspolXstart] = '#';

        planszaWyswietlanie();

        //wpisywanie jednomasztowca 3
        int straznik15 = -9;
        int straznik16 = 0;
        while (straznik16 != 2) {
            straznik15 = -9;
            straznik16 = 0;

            while (straznik15 == -9) {
                System.out.println("Podaj współrzędną X jednomasztowca trzeciego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu X");
                } else {
                    straznik15 = -8;
                }
            }

            while (straznik15 == -8) {
                System.out.println("Podaj współrzędną Y jednomasztowca trzeciego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu Y");
                } else {
                    straznik15 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2 i 2_1 i 2_2 i 2_3 i 1_1 i 1_2 i 1_3
            for (int i = 0; i < 1; i++) {
                if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if ((plansza[wspolYstart][wspolXstart]) == '#') {
                    i = 4;
                }

                if (wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (i == 4) {
                    System.out.println("Statek jest za blisko innego");
                } else {
                    straznik16 = 2;
                }
            }
        }

        //dodawanie jednomasztowca 3 do tablicy
        plansza[wspolYstart][wspolXstart] = '#';

        planszaWyswietlanie();

        //wpisywanie jednomasztowca 4
        int straznik17 = -9;
        int straznik18 = 0;
        while (straznik18 != 2) {
            straznik17 = -9;
            straznik18 = 0;

            while (straznik17 == -9) {
                System.out.println("Podaj współrzędną X jednomasztowca czwartego");
                wspolXstart = pisanie.nextInt();
                if (wspolXstart < 1 || wspolXstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu X");
                } else {
                    straznik17 = -8;
                }
            }

            while (straznik17 == -8) {
                System.out.println("Podaj współrzędną Y jednomasztowca czwartego");
                wspolYstart = pisanie.nextInt();
                if (wspolYstart < 1 || wspolYstart > 10) {
                    System.out.println("Niepoprawna współrzędna punktu Y");
                } else {
                    straznik17 = -7;
                }
            }

            //sprawdzanie czy jest przerwa między statkami 4 i 3_1 i 3_2 i 2_1 i 2_2 i 2_3 i 1_1 i 1_2 i 1_3 i 1_4
            for (int i = 0; i < 1; i++) {
                if (wspolYstart - 1 > 0 && wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0) {
                    if ((plansza[wspolYstart - 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart - 1 > 0 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart - 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if ((plansza[wspolYstart][wspolXstart]) == '#') {
                    i = 4;
                }

                if (wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolXstart - 1 > 0) {
                    if ((plansza[wspolYstart + 1][wspolXstart - 1]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart]) == '#') {
                        i = 4;
                    }
                }

                if (wspolYstart + 1 < 11 && wspolXstart + 1 < 11) {
                    if ((plansza[wspolYstart + 1][wspolXstart + 1]) == '#') {
                        i = 4;
                    }
                }

                if (i == 4) {
                    System.out.println("Statek jest za blisko innego");
                } else {
                    straznik18 = 2;
                }
            }
        }

        //dodawanie jednomasztowca 4 do tablicy
        plansza[wspolYstart][wspolXstart] = '#';

        planszaWyswietlanie();
    }

    void strzelanieDoMoich() {//rysowanie po mojej planszy trafien przeciwnika
        int sprawdzanie1 = 0;
        System.out.println("Gdzie strzelał przeciwnik?");

        while (sprawdzanie1 != 1) {
            System.out.println("Podaj współrzędną X");
            strzelanieX = pisanie.nextInt();

            if (strzelanieX > 0 && strzelanieX < 11) {
                sprawdzanie1 = 1;
            } else {
                System.out.println("Podano złą współrzędną X");
            }
        }

        sprawdzanie1 = 0;

        while (sprawdzanie1 != 1) {
            System.out.println("Podaj współrzędną Y");
            strzelanieY = pisanie.nextInt();

            if (strzelanieY > 0 && strzelanieY < 11) {
                sprawdzanie1 = 1;
            } else {
                System.out.println("Podano złą współrzędną Y");
            }
        }

        if (plansza[strzelanieY][strzelanieX] == '#' || plansza[strzelanieY][strzelanieX] == '@') {
            plansza[strzelanieY][strzelanieX] = '@';
            System.out.println("Trafiono!");
        } else {
            plansza[strzelanieY][strzelanieX] = 'X';
            System.out.println("Pudło!");
        }
    }

    void strzelanieDoPrzeciwnika() {//zapis trafień i pudeł w statki przeciwnika
        int sprawdzanie1 = 0;
        int sprawdzanie2;
        System.out.println("Gdzie strzelasz?");

        while (sprawdzanie1 != 1) {
            System.out.println("Podaj współrzędną X");
            strzelanieX = pisanie.nextInt();

            if (strzelanieX > 0 && strzelanieX < 11) {
                sprawdzanie1 = 1;
            } else {
                System.out.println("Podano złą współrzędną X");
            }
        }

        sprawdzanie1 = 0;

        while (sprawdzanie1 != 1) {
            System.out.println("Podaj współrzędną Y");
            strzelanieY = pisanie.nextInt();

            if (strzelanieY > 0 && strzelanieY < 11) {
                sprawdzanie1 = 1;
            } else {
                System.out.println("Podano złą współrzędną Y");
            }
        }

        sprawdzanie1 = 0;

        while (sprawdzanie1 != 1) {
            System.out.println("Trafiony?");
            System.out.println("1 - TAK, 2 - NIE");
            sprawdzanie2 = pisanie.nextInt();

            if (sprawdzanie2 != 1 && sprawdzanie2 != 2) {
                System.out.println("Nie wybrano prawidłowo opcji");
            } else if (sprawdzanie2 == 1) {
                plansza1[strzelanieY][strzelanieX] = '@';
                sprawdzanie1 = 1;
            } else if (sprawdzanie2 == 2) {
                plansza1[strzelanieY][strzelanieX] = 'X';
                sprawdzanie1 = 1;
            }
        }
    }

    void planszaPustaTworzenie1() {//tworzenie pustej planszy na strzały
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                plansza1[i][j] = 'O';
            }
        }
    }

    void planszaWyswietlanie1() {//wyświetlanie planszy do strzałów
        int licznik = 0;
        int licznik1 = 0;
        System.out.println("  Legenda: @ - trafiony, X - pudło");
        System.out.println("  Plansza do strzelania");
        System.out.println("   A B C D E F G H I J");
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        System.out.println("--------------------------");
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                if (j == 1) {
                    if (i == 10) {
                        System.out.print(++licznik + "|");
                    } else {
                        System.out.print(++licznik + " |");
                    }
                    System.out.print(plansza1[i][j] + " ");
                } else if (j == 10) {
                    System.out.print(plansza1[i][j] + " ");
                    if (i == 10) {
                        System.out.print("|" + ++licznik1);
                    } else {
                        System.out.print("| " + ++licznik1);
                    }
                } else {
                    System.out.print(plansza1[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------");
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        System.out.println("   A B C D E F G H I J");
    }
}

/**
 *
 * @author Kamil
 */
public class GraWStatki {

    //public static int Enter = 1000;
    //public static int pozycjaMenu = 1;
    //public static char wybranyPrzycisk;
    //String obecnyLogin = "NoNe098";
    Scanner in = new Scanner(System.in);

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        Scanner pisanie = new Scanner(System.in);
        int dzialanie = 3;
        int sprawdzanie = 0;
        int zaczynanie;
        menu menu = new menu();
        uzytkownik uzytkownik = new uzytkownik();
        Gra gra = new Gra();

        //    System.out.println("Wynik: " + proba);
        while (true) {
            switch (dzialanie) {
                case 1: // wczytywanie uzytkownika 
                    dzialanie = uzytkownik.wczytywanieUzytkownika();
                    break;
                case 2: // tworzenie uzytkownika
                    //System.out.println("Tworzenie uzytkownika");
                    dzialanie = uzytkownik.dodawanieUzytkownika();
                    break;
                case 3: //menu logowania
                    dzialanie = menu.logowanie();
                    break;
                case 4: //menu gry
                    //System.out.println("Menu gy");
                    dzialanie = menu.menu();
                    break;
                case 5://nowa gra
                    gra.planszaPustaTworzenie();
                    gra.dodawanieStatkow();
                    gra.planszaPustaTworzenie1();

                    gra.planszaWyswietlanie();
                    gra.planszaWyswietlanie1();

                    while (true) {
                        System.out.println("Kto zaczyna grę?   1 - Ja, 2 - Przeciwnik");
                        zaczynanie = pisanie.nextInt();
                        if (zaczynanie != 1 && zaczynanie != 2) {
                            System.out.println("Podano niewłaściwą cyfrę");
                        } else {
                            break;
                        }
                    }

                    if (zaczynanie == 2) {
                        while (true) {
                            gra.strzelanieDoMoich();
                            gra.planszaWyswietlanie();

                            gra.planszaWyswietlanie1();
                            gra.strzelanieDoPrzeciwnika();
                            gra.planszaWyswietlanie1();

                            System.out.println("Gramy dalej? 1 - Tak, 2 - Nie");
                            sprawdzanie = pisanie.nextInt();
                            if (sprawdzanie == 2) {
                                dzialanie = 4;
                                break;
                            }
                        }
                    } else {
                        while (true) {
                            gra.planszaWyswietlanie1();
                            gra.strzelanieDoPrzeciwnika();
                            gra.planszaWyswietlanie1();

                            gra.strzelanieDoMoich();
                            gra.planszaWyswietlanie();

                            System.out.println("Gramy dalej? 1 - Tak, 2 - Nie");
                            sprawdzanie = pisanie.nextInt();
                            if (sprawdzanie == 2) {
                                dzialanie = 4;
                                break;
                            }
                        }
                    }

                default:
                    break;
            }
        }

        //uzytkownik uzytkownik = new uzytkownik();
        //uzytkownik.wczytywanieUzytkownika("Krzychu");
        //uzytkownik.dodawanieUzytkownika();
        //menu.logowanie();
        //menu.menu();
        //Gra Gra = new Gra();
        //Gra.planszaPustaTworzenie();
        //Gra.planszaWyswietlanie();
        //Gra.dodawanieStatkow();
        //menu.logowanie();
    }

}

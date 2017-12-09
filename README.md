# IDW
### Popis programu

Program načte soubor, ve kterém je na prvním řádku napsaný počet řádků, na dalších řádcích jsou pak napsané souřadnice x, y a jejich naměřená hodnota. V souboru musí být místo desetinné čárky desetinná tečka a čísla musí být od sebe oddělená čárkou. Po načtení souboru program převede načtená čísla na jejich numerické hodnoty a vytvoří síť 100x100. Krajní hrany sítě obsahují body s minimálními respektive maximálními souřadnicemi načtených bodů. Na základě načtených bodů program provede interpolaci jednotlivých bodů v síti pomocí metody IDW. Po dokončení interpolace zaokrouhlý výsledné hodnoty na dvě desetinná místa a zapíše je do výstupního souboru.

### Funkcionalita
Program začne try blokem, kde vyzkouší otevřít soubor a načíst první řádek. Tento blok je odchycen třemi chatch bloky, které odchytávají výjimky. V dalším try bloku již načte program všechny řádky, oddělý je podle čárky a zavolá metodu, která převede string na numerické hodnoty. Po vytvoření polí, které symbolizují mřížku 100x100 je volána metoda pro naplnění polí, aby krajní hodnoty obsahovaly minimální respektive maximální souřadnice načtených bodů. Metoda se skládá ze tří cyklů a podmínky. V mainu je vytvořeno pole save a volána metoda pro výpočet vzdáleností. Tato metoda se skládá ze dvou cyklů, čtyř podmínek a volá metodu idw, která se skládá ze tří cyklů a počítá konečnou hodnotu interpolovaného bodu. Hodnotu vrací a ta je uložena do pole save. Po dokončení interpolace všech bodů, program otevře výstupní soubor a zapíše do něj výsledné hodnoty. Tato operace je ošetřena catchem, kdyby soubor nebyl nalezen, program se ukončí.

                                                                                                        Dominik Mazur

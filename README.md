# IDW

### Popis programu
Program je neinteraktivní a k jeho spuštění je potřeba zadat parametry ve formě vstup.csv výstup.csv. Pokud chce uživatel zadat exponent „-p“, který bude použit ve váhové funkci, pak je nutné ho napsat před vstup a výstup. Příklad: -p 10 vstup.csv vystup.csv.

Program načte soubor, ve kterém je na prvním řádku napsaný počet řádků, na dalších řádcích jsou pak napsané souřadnice x, y a jejich naměřená hodnota. V souboru musí být místo desetinné čárky desetinná tečka a čísla musí být od sebe oddělená čárkou. Po načtení souboru program převede načtená čísla na jejich numerické hodnoty a vytvoří síť 100x100. Krajní hrany sítě obsahují body s minimálními respektive maximálními souřadnicemi načtených bodů. Na základě načtených bodů, program provede interpolaci jednotlivých bodů v síti pomocí metody IDW. Po dokončení interpolace jsou výsledné hodnoty zaokrouhleny na dvě desetinná místa a program je zapíše do výstupního souboru.

### Funkcionalita

Program při načítání souborů, nebo jiných akcí, které by mohli vyhodit výjimku, používá bloky try a catch pro ošetření výjimek. Na začátku se volá metoda pro načtení prvního řádku, číslo je parsováno a slouží k vytvoření dvourozměrného pole. Následně se načte zbytek řádků, které obsahují souřadnice a hodnoty. Znaky v řádku jsou rozlámány podle čárky a jsou převedeny na numerické hodnoty. Tyto hodnoty jsou vráceny do dvourozměrného pole v mainu jménem coor. Následně program přiřadí defaultní hodnotu 2 exponentu na výpočet vah, pokud není zadán parametr „-p“, který by korektně toto číslo změnil. Následuje vytváření polí, které symbolizují mřížku 100x100. Dále je volána metoda pro naplnění polí, aby krajní hodnoty obsahovaly minimální respektive maximální souřadnice načtených bodů. V mainu se vytvoří pole result, které slouží pro uložení konečných výsledných hodnot. Výsledné hodnoty jsou vypočítány pomocí metody, která počítá vzdálenosti mezi bodem interpolovaným a body načtenými a následně volá metodu pro výpočet IDW, která vrátí konečnou hodnotu interpolovaného bodu. Hodnota je uložena do pole result. Po dokončení interpolace všech bodů, program zavolá metodu pro zápis, otevře výstupní soubor a zapíše do něj výsledné hodnoty.



                                                                                                        Dominik Mazur

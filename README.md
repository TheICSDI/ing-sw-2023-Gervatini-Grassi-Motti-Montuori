
# Software engineering project
<img src="src/main/resources/Images/logo.png" width=350ppx height=350px align="right"  alt="MyShelfie Logo"/>
The final project of Software Engineering, course of Computer Science Engineering held at Politecnico di Milano (2022/2023). <br/>
It consists of the implementation of a distributed system composed of a single server capable of managing more games at a time and multiple clients (one per player) using the MVC pattern (Model-View-Controller). <br/>

<br/> **Group**: PSP-35

**The team**: 
- [Andrea Grassi](https://github.com/Fozyhh)
- [Marco Gervatini](https://github.com/Shift007)
- [Giulio Montuori](https://github.com/TheICSDI)
- [Caterina Motti](https://github.com/mttcrn)

## Implemented features

| Feature | Implemented |
| ------- | ----------- |
| Complete rules | :heavy_check_mark: |
| CLI | :heavy_check_mark: |
| GUI | :heavy_check_mark: |
| Socket | :heavy_check_mark: |
| RMI | :heavy_check_mark: |
| Multiple matches (FA 1) | :heavy_check_mark: |
| Resilience to disconnections (FA 2) | :heavy_check_mark: |
| Chat (FA 3) | :heavy_check_mark: |

## Testing
Almost all `model` and `controller` classes have a class and method coverage of 100% (except for trivial methods like getters and setters).

| Package      | Class              | Coverage (lines) |
|--------------|--------------------|------------------|
| Model        | Entire Package     | 95% (821/856)    |
| Controller   | Entire Package     | 92% (364/395)    |
| Controller   | clientController   | 91% (140/153)    |
| Controller   | gameController     | 87% (57/65)      |
| Controller   | serverController   | 94% (143/151)    | 

## Requirements
To successfully execute the `.jar` applications, which it can be found [here](https://github.com/TheICSDI/ing-sw-2023-Gervatini-Grassi-Motti-Montuori/tree/main/deliverables/final/jar), both Windows and Java 20 are required.

If you wish to compile the project independently, it is necessary to either download the GitHub repository or clone it using the following command:
```properties
git clone https://github.com/TheICSDI/ing-sw-2023-Gervatini-Grassi-Motti-Montuori.git
```
Afterwards, it can be compiled using your IDE of choice, provided with the required dependencies, `java-20-openjdk` and `javafx-sdk-20.0.1`.

## Run using the JAR file
Once installed all requirements and/or compiled the project, open a terminal and
go to the `.jar` files directory. 
Once there it is possible to choose to run the server or the client (CLI or GUI):

### Run the Server
```properties
java -jar Server.jar [-i <Your IPv4> | --ip <Your IPv4>]
```
- If the parameters `ip` or `i` are omitted, the server will run on `localhost`.
### Run the Client (CLI)
```properties
java -jar Client.jar -c
```
or
```properties
java -jar Client.jar --cli
```
### Run the Client (GUI)
```properties
java -jar Client.jar
```

To run the Client (GUI) it is also possible to open the JAR file directly from
the file explorer.

***WARNING***: For the best GUI experience it is strongly suggested to play with a screen resolution
of 1920x1080 (100% DPI) and with a scaling of 100%.

## License

This project is developed in collaboration with [Politecnico di Milano](https://www.polimi.it) and [Cranio Creations](http://www.craniocreations.it).

_NOTA: My Shelfie è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti._

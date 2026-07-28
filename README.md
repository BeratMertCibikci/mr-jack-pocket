# mr-jack-pocket
    javac -d out src/model/*.java src/main/*.java src/engine/*.java src/ai/*.java src/IHM/*.java src/reseau/*.java
    java -cp out IHM.FenetrePrincipale

    IHM:
    javac -d out src/*/*.java
    java -cp out IHM.FenetrePrincipale

mr-jack-pocket/
│
├── README.md
│
├── src/
│   ├── model/
│   ├── engine/
│   ├── actions/
│   ├── ihm/
│   ├── ia/
│   ├── controller/
│   └── utils/
│
├── assets/
│   ├── images/
│   └── sounds/
│
├── tests/
    ├── model/
    ├── engine/
    ├── ia/
    └── integration/


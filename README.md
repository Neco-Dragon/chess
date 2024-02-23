# ♕ BYU CS 240 Chess

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsND1i7AjTGTTBHi83vuHT9hS7AeQwRDZR87fDEV6YN4cgq+U2bNcT1PMC0uC87lNFBWQtK0bW9ap7ydONIldSVpXfW15S7CsZ3VTUy2jR1Y1rKJqBLNMrW7LNTzIxdGxgVt21KXDMx6TA+wHcwrFsMwUFDSd2EsZgbD8AIgmQBt-lPeIEhkV90mBTdty4Xd7HTPCTDo88SMvDQUAQJ4UDg60y0gTNkNIp94gU8ElKo8tMxgAAxFgUgAWUcmBkB4YiH2dBMc1Ag14i4CxY2A3VThTQ1BlkmBwsinTBgY5s2w7JKMRgaYuGwQR9A4rjTB44dpg0Cc3BgABxO1MTEudJJCZh4pXOTquBLJcnYO1ijYysdIgvSoJgHy3Fq6ouDmKzsoiV0WE6lgxWBAA1YErzqmAkBnIx-JQmS2pqu1QTcKsKLPOspIwWJmI7CaUBOnguhgYoACIesm174hKd67TFGQvoAOmB3s0H7Eqh1sbALCgbAjPgM0DHu2cJIXK6WuXc65PXLqcg+lA+s0zMO3xiU7RPFcIh1Klk0JFBYmZGDAnuuZSbtA5qdzA05ufDCPShfHvi2jAIABKKabA2lC3ImIwoijEzv6aW0qYjLSiy4VcvytBCs4sHuMhmxHEMhAIG8GAACkIG2o7qlsbQEFAIM0eag6sbXZ5cfxwnqLwjsGLgCBTagOp8f+inzouY0RuvFmw5kOoA6D6AZruHnxXdT192FyAYBAdHA+Dsnqj26ygpAmK6fiBQEBZDRnDIa58BQCrxa52Ko8gxkYAAK2ttAFC4FJtAKGJWb+mRU+dNDnwW9IltW9b8d-f92jAEB-DQdgNuqf7S9mqnExC2KHjtNvj7p2sEvxxXKELFXbtKfHQfBwdeJsMxgGcRAYNgYBsDhoQUCKN5wMTdrLRIdlOrKVyLtU8ERo7d3zr-aa+807hFdFAhy5k8LqEQVwdOMAsHrX3HgruBCMHPmITvVQaDAqRE5hffMqYNZqHPpXZhcVMYQNYeoFKl1mrpRYiUXhOVVDa11iYLiQA

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

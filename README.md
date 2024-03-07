# ♕ BYU CS 240 Chess

[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHusFJeIYFEEFWkGh1VMKbN+etx6pMdrXUcTkSxv2UFq7q7dUzyJ9vlyrjygSCYDvbs6N9D3c30V6JHpZQGpTAZZuYMcwFiJc6lGQcFgPbE3WeE93i+H4ZhqOprwgUE73kB9DyfD1XwRGB30-VcGV-B55XxXxlVVedNQwyDjxZA0jUzcM-RgWdIBgCjPHvYj6WfXp2CiJjfUjQi+JHQSLWE-0Rxjfx4yTVN0yE7N2kwPMC10AxjB0FA7UrLR9GYWtvF8TA5KbXpWz4M8kjeNJ0i7CQezyZS-U0GTsPheVXEBJAoBKTjRlc9AV09NdpRdaFvLQXz-MC4K0HWGidR6PVrK+Wywyk5DQU47jIt4zyBLwj9XS-CcxJbKAonwsqPJ6cyExgFM0wKWroUGCRsFcFRVPU7RNOLQYZArYYYAAcR5R5jPrMzG2YTzW3Gt4O3SLQeRcyTsyjUd+K9EAfL8gKLSCra-VCnCiIKx5oti47KISiCUugydTzg34EpyjiLXyx9CrHMKatKh5yvCn9rqiGBkAcSaygkUYnqPEI9Vg89-ym1i3FhlBfsw-69tUKJsY+YZQZjcSJp5EmHB2hlGoiFr02J4ZWhgPIACJ1rh9monyTmeWZPgeYAOlF3M0HzAai2MbA9CgbAEFUOAyNUbGPBMhsAgWl8aGqmIEmSeyuZQTasz9dNjdZHlhyqhlV1uo74rOkKyfXPGbtxQ64pOx7kqRl7XjPeDPuBFDvtVXHaOfe2SoIsKrr+m6FQJbHRkt6ieIkZGTzZDlfmN25MfY7HI5SoqvWNwXXcWvXK74Wm+Pp5rFIKOvxclwstKMcwUBRCB3BgAApCBZ0pspjAUBBQGtOatbtqqojiU5VuN03mPQdNGrgCBpygSo24bnXioOmLHZ953EtdiLE9ek+7qds2Xcz49XtR4OL6+vL0Of6PAdjur45u0gluM4Fw9yIyfNBQO71jQAlDqhbIpckbl1wnuK+z8QGpzrhA3iUC3pozrl9Eu39rrkz-u1KQ6CIYwAAFYjzQFggWfBKhbx3tAHBWdgh6lzoaC8pJx5sQgGPHGJCb6VRwkDOOl1f5dCODyHaz4m6M1btbNSEsNLSyMDoYAlhECKlgMAbACtCDOFcOrWajV56yJiOlFadkMjqAUUfCc8pFZ4ARlfcGidXH6K-kIDxmds5RFsZlJikA-SPHgeHLioj3ZBhjhQ6Qolr7ux8XgNC-iOFBJ-DZN4sCMZRIycAP2WEAYSP-iDZJXjUm4jcVAYhmSSlSGySEvJxtIk3gacUn+KDJEAOkTXPplTbaN3mgpVq+REkwE6t1NAvVNBqPzEAA)

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

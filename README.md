# Alexa Skill Development
> The skill is developed based on the HelloWorld sample.

## Objective

The objective of this project is to design and develop an Alexa skill. In the meantime, sort out the needed work of developing a voice-interact application based on the existing technology provided by Amazon Alexa. 

Considering that voice interaction is suitable to be applied in situations where it is not convenient to interact with a screen by hand, this skill is a walkthrough tool of a video game called *The Legend of Zelda: Breath of the Wild*. It provides a new option for the in-game players who encounter problems. When the users need information about recipes or materials of the game, instead of searching on the Internet, they can ask Alexa questions, and Alexa will query the database for them and give the response. 

## Requirements

### Technical requirement

The development of an Alexa skill requires two main parts, the interaction model and the back-end logic. If the skill needs to read or write persistent data, it will also need a database to be connected with the back-end program. Alexa will interact with the user directly. It will analyse the sentence spoken by the user according to the model set by the developer, then pass the user's input to the back-end logic of the intent for corresponding processing and generate an appropriate response to speak to the user. 

Alexa provides a developer console for developers to build the interaction model by configuring different “user intents”, “utterances” and “slots”. When a user speaks a specific utterance defined for this intent, the intent can be recognized by Alexa. An utterance may include slots as input information. The developer can define whether a slot must be fill for an intent. If a slot is necessary, then the utterances defined for the intent should all include the slot. If the user does not mention the slot, follow-up conversations must be added to obtain the value of the slot. The possible values of each slot must be defined. Alexa provides some commonly used pre-set slots, such as the date and four-digit numbers. For custom slots, developer must define the type of the slot and list all possible values.

Back-end Logic supports to be written in different languages including java, python and Node.js, as long as the code follows the API provided by Amazon. The program should contain handlers to process the input of all kinds of intents, for example, a handler can obtain the slot value, query a database and generate a response text with the result. The back-end code needs to be uploaded to Amazon AWS to run as a Lambda function and linked to the specific skill ID, so that the skill can communicate with the back-end program. This project uses Java as the language of the back-end program and PostgreSQL as the database.

### Functional requirement

#### Theme

The objective is to design and develop an Alexa skill. Alexa skills offer the users a different way to access applications that they used to access on phone or computer. The main difference is that voice interaction does not require the use of both hands and can be achieved simply by using language to issue commands or ask questions. As a result, many of these apps are designed for situations where it is inconvenient to use your hands. For example, check email while driving, and check recipes while cooking.

Think of another situation where the game players' hands may not be free when they're playing video games. It will be troublesome if the player needs to query some game walkthrough during gaming. In case of facing problem in the game, one option for the players is to pause the game, put down their gamepad, and use a computer or mobile phone to search the Internet for an answer. An Alexa skill can provide a new option, that is, the player only needs to interact with Alexa by speech to get the desired answers, instead of spending time on operating other devices by hand. This Alexa skill is designed considering this possible user requirement. 

The name of the skill is “The cookbook of Link”, in which “Link” is the hero of a video game called The Legend of Zelda: Breath of the wild. In this game, players can control the character to defeat the enemies and experience an adventure. The game also involves a complex cooking system. The character Link can collect materials to cook different dishes or elixirs that provide various special effects.

In different situations, Link may need different special effects, such as healing, attack boost and cold-resistant. A dish consists of one to five ingredients, including herbs, meats, mushrooms and “monster parts”, the special material that may drop from monsters. In total, there are hundreds of kinds of ingredients and nearly a hundred kinds of dishes. The system involves a large amount of information so that even skilled players need to refer to game walkthroughs sometimes. 

#### User requirements

In many of *Zelda: A Breath of the Wild* online game guides, the information around the topic of cooking includes recipes and effects of various dishes, types and recipes of elixirs, and methods of obtaining monster parts (Zelda Breath of the Wild Crafting Recipes | Food Cooking Ingredients, 2020). The cookbook of Link considers the game players as target users, providing these recipes and related information as to its main feature, to take the place of the online walkthroughs. Therefore, the functionalities of this application are to allow users to ask questions about specific information, then give responses according to the data provided by the game walkthroughs. Some questions should contain an input as the condition of a query, and the response will include a specific output as the answer. 

| **Name**               | **Requirements**                                             | **Input**      | **Output**                |
| ---------------------- | ------------------------------------------------------------ | -------------- | ------------------------- |
| **Ingredient to dish** | Learn about what dishes can a specific ingredient make.      | An ingredient  | One or more dishes        |
| **Find dish**          | Learn about what ingredients does a  specific dish need and what effect does it provide. | A dish         | Ingredients and effect    |
| **Find effect**        | Learn about a dish that provides a specific effect.          | An effect      | A dish                    |
| **Find monster part**  | Learn about how to get a specific monster  part.             | A monster part | One or more monsters      |
| **Monster drops**      | Learn about what can a specific monster drop.                | A monster      | One or more monster parts |
| **Find elixirs**       | Learn about the kinds of elixirs.                            | Null           | Categories of elixirs     |
| **Find mushroom**      | Learn about the kinds of mushrooms.                          | Null           | Categories of mushrooms   |

This table includes the possible requirements of the users, the critical content of both the input requests and the output responses. 

In addition to these detailed requirements, the most basic requirement is that Alexa must be able to understand the questions asked by the user in different sentence structures and give answers consistent with the user's intentions. Different users may have different language habits, and the same problem can be expressed in many different sentences in English. Ideally, these expressions can be accurately identified by the interaction model.
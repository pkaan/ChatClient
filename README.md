
ChatClient

![chat_5](https://user-images.githubusercontent.com/63013313/147917675-ea7b713e-b243-477b-9f0e-b45e0cd10743.png)

To start the client:

1. Make sure server is running (recommended server version 5)

![server_start](https://user-images.githubusercontent.com/63013313/147917942-d35b38d1-548c-42ad-86f4-921d9116ca7a.png)


2. Run the client with following command/commands:


   > java -jar target/ChatClient-0.0.1-SNAPSHOT-jar-with-dependencies.jar [server version] [location of client certificate]

   or

   > mvn package
   > java -jar target/ChatClient-0.0.1-SNAPSHOT-jar-with-dependencies.jar [server version] [location of client certificate]
   
   for example:
   java -jar target/ChatClient-0.0.1-SNAPSHOT-jar-with-dependencies.jar 5 client.cer

![client_start](https://user-images.githubusercontent.com/63013313/147917936-4f5139fb-e5c7-4708-8333-810e6bc8aa5d.png)

3. Login/register

![chat_1](https://user-images.githubusercontent.com/63013313/147917627-33208530-df66-45d8-aca5-51bf7aaa7c7f.png)
![chat_3](https://user-images.githubusercontent.com/63013313/147917647-eeef353a-c4d1-455d-9ddc-b01df991f92f.png)
![chat_4](https://user-images.githubusercontent.com/63013313/147917657-b734426d-5671-4855-a1df-6a58b6f411a7.png)

4. To get messages automatically retrieved from the server, turn the autofetch on

![chat_6](https://user-images.githubusercontent.com/63013313/147917684-5af794ca-3068-4d66-9036-e20aea22d522.png)

- Dark theme also available

![chat_2](https://user-images.githubusercontent.com/63013313/147917636-5f50a74d-8a22-4cce-9cf5-019968eb5c0d.png)
![chat_2_2](https://user-images.githubusercontent.com/63013313/147918129-dc350bd4-ddec-4a48-8300-b45b6374f936.png)

! To change the server

IP address, the client and keystore.jks needs to be modified. 

KeyStore.jks -> 
Dns and ip addresses needs to be added to the list. 
keytool -genkey -alias alias -keyalg RSA -keystore keystore.jks -keysize 2048 -ext SAN=dns:[host_ip],ip:[host_ip]

Client.cer -> 
keytool -export -alias alias -keystore keystore.jks -rfc -file client.cer 

Change server -> (port the server is using)
https://ip:port/ 

Two ChatClients connected to the same server in the same local area network
![chat_8](https://user-images.githubusercontent.com/63013313/147919206-cbcae525-29ea-4d95-810d-973518612d60.jpg)




Other:

- Chat window font is adjustable
- User can also change their nickname
- Changing the server while logged in will logout the user
- BE NICE TO OTHERS!




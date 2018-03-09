# twitchTTS
comments on twitch to speech

This project used Yandex speechkit and 
https://github.com/pircbotx/pircbotx

First of all you need yandex api key 
https://tech.yandex.ru/speechkit/cloud/

To can generate your Twitch oauth: https://twitchapps.com/tmi/
For windows start jar with -Dfile.encoding=UTF-8
TODO:
1) add application.properties into resources folder
    - add property yandex.tts.key = {*YOUR API KEY*}
    - add property channels = #channel1, #channel2
    - add property nick = {*YOUR NICK*}
    - add property oauth = {*YOUR OAUTH*} 

2) enjoy

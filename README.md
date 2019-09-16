Hack The North 2019 submission
## Inspiration
Travelling to an unknown region in the world comes with some inconveniences, such as not having the slightest clue about the currency conversion rate and having to fumble through your phone to try and googling the rate. However, oftentimes, we might not necessarily know the name of the currency itself which makes even googling a difficult task. For this reason, we came up with the idea of Travel CC, a currency conversion Android app that utilizes the device's camera to scan prices and converts them automatically, making the entire process hassle-free.
## What it does (should do)
Travel CC automatically retrieves your destination country's currency and also your device's set country's currency. From there, the user can take a picture of a price tag and Travel CC will use the image to recognize the price and convert it accordingly to the user's home country currency.
## How we built it
We built the app using Android Studio and leverage several APIs, such XE Currency Data API to accurately convert the currency and Firebase API for the text recognition using an on-device model.
## Challenges we ran into
We had a difficult time dealing with the image to text conversion. We first wanted to use Google's Cloud API, but we were unable to make it function properly and decided to try the Firebase API instead. However, we could not finish debugging the implementation of Firebase API within our app in time.
## Accomplishments that we're proud of
Having developed the idea from scratch and having completed architecting the structure of our app.
## What we learned
We learned that we should have completed the core functionality of our app first (image text recognition) before diving into the other implementation details.
## What's next for Travel CC
Fixing the image recognition functionality, incorporate additional QOL features such as automatic location detection, making the detection and conversion in real-time.

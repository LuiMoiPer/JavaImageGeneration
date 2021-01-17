# JavaImageGeneration
Making (hopefully) interesting images with java.\
[Code in action](https://twitter.com/bot_transformer)

## Description
This project downloads a trending image from Twitter, transforms it, and post the transformed image to twitter.
The transformed image is posted as a reply to the tweet the image was grabbed from as well as an individual tweet that embeds the original tweet.
The program expects keys and tokens to be placed in the `Candy` folder under the following names without file extensions:
* `AccessToken`
* `AccessTokenSecret`
* `ApiKey`
* `ApiSecretKey`
* `BearerToken`

Transformed images are saved locally in the `Outputs` folder before being posted.

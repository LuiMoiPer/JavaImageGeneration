# JavaImageGeneration
Making (hopefully) interesting images with java

## Description
This project downlads a trending image from twitter, transforms it and post the transformed image to twitter.
The transformed image is posted as a reply to the tweet the image was grabbed from as well as an individual tweet that embeds the original tweet.
The program expect keys and tokens to be placed in the `Candy` folder under the following names without file extensions:
* `AccessToken`
* `AccessTokenSecret`
* `ApiKey`
* `ApiSecretKey`
* `BearerToken`

Transformed images are saves locally in the `Outputs` folder before being posted.

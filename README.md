# bossBouncer
Rate your Boss
Boss Bouncer- Rating App


Application Flow:

1.	User Registration:

Users register on the website by providing their details and creating an account.

2.	Boss Rating:

Users see a page where they can enter the name and title of their boss, along with their rating (up, down, or neutral).
When a user submits a rating, a new entry is created in the Rating table, associating the user, the boss, and the rating.
The timestamp is recorded to track when the rating was given.

3.	Confirmation and Payment:

After submitting the rating, users are presented with a confirmation screen that displays the boss's details, the rating, and the nominal fee ($2).
If the user confirms the rating, a payment entry is created in the Payment table, associating the user and the payment amount.
The timestamp is recorded to track when the payment was made.

4.	Sharing and Analytics:

Users can share their ratings through a share button, which could generate a unique URL or utilize social media sharing APIs.
Analytics can be implemented to track the number of ratings, the average ratings for each boss, and other relevant metrics

Database tables:
![image](https://github.com/nkchauhan/bossBouncer/assets/6480681/30424136-661b-4b4c-858b-45b9338092cf)

Flow Chart:
![image](https://github.com/nkchauhan/bossBouncer/assets/6480681/3125e55b-3d9d-4891-85bf-224034af5910)


# Twitter-US-Airlines-Sentiment-Analysis
This is a kaggle completetion you can refer to [Top description](https://www.kaggle.com/crowdflower/twitter-airline-sentiment)
it is about analyzing how travelers in February 2015 expressed their feelings on Twitter


## What we do
You can see on the kaggle, people maily use R or other tools did some basic analysis of the original data. Like word frequent, proportion of tweets with each sentiment, etc.

### Basic analysis
1. overall sentiment(negative, neutral, positive percentage)
2. perfectage of tweets per airline
3. Proportion of negative/neutral positive sentiment tweets per airline
4. Reasons for negative sentiment tweets
5. Reasons for negative sentiment per airline
6. Tweet location exploration
7. Tweet timezone study
8. Location of tweets: Visualization on maps

above were using pig script on Hadoop cluster, and tableau.

### Advanced analysis
1 Find top 300 negative words in the negative comments in decreasing order 
2 Find top 300 positive words in the positive comments in decreasing order 
3 Choose 5-10 from top negative words, find their top 100 co-occurrence words in negative comments. 4 Choose 5-10 from top positive words, find their top 100 co-occurrence words in the positive words

### Machine learning 
We try to predict random tweets sentiment:
1. transfer the original data to Words of bag model.
2. Build different Machine learning models including Decision tree, Logistic Regression, Naive Bayes, Random Forest, Logistic Regression 
and neural network.

#### Result
For detailed result, you can refer to Presentation PPT

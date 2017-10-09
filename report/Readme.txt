

analysis part 1 task list:
1. overall sentiment(negative, neutral, positive percentage)
2. perfectage of tweets per airline
3. Proportion of negative/neutral positive sentiment tweets per airline
4. Reasons for negative sentiment tweets
5. Reasons for negative sentiment per airline
6. Tweet location exploration
7. Tweet timezone study
8. Location of tweets: Visualization on maps

analysis part2 task list:
1 Find top 300 negative words in the negative comments in decreasing order2 Find top 300 positive words in the positive comments in decreasing order3 Choose 5-10 from top negative words, find their top 100 co-occurrence words in negative comments.4 Choose 5-10 from top positive words, find their top 100 co-occurrence words in the positive words



machine learning analysis part 1 task list:
//convert css to libsvm file(convert.py) (for Decision tree, Logistic Regression, Random Forest) 
eg: python convert.py sample_train.csv train.csv

//template.jar

//for 3 class label
Decision tree:
spark-submit --class template.template.dt --master yarn template.jar 2.csv 30 entropy 3

Logistic Regression
spark-submit --class template.template.lr --master yarn template.jar 2.csv 3

Naive Bayes:
spark-submit --class template.template.naive --master yarn template.jar 4000.csv

Random Forest:
spark-submit --class template.template.random --master yarn template.jar 2.csv 3

//for 12 class label

Decision tree:
spark-submit --class template.template.dt --master yarn template.jar 1.csv 30 entropy 12

Logistic Regression
spark-submit --class template.template.lr --master yarn template.jar 1.csv 12

Naive Bayes:
spark-submit --class template.template.naive --master yarn template.jar TweetsAll.csv

Random Forest:
spark-submit --class template.template.random --master yarn template.jar 1.csv 12


machine learning analysis part 2 task list:


First, make sure installed numpy, sklearn, re and naiveBayesClassifier package in Python 2.7.Change the path to each python file.Then, you need to add permission to each py file, using commend line, for example typing:chmod +x code.pyLast, run the code, for example typing:./code.py



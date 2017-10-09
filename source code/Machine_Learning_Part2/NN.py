#!/usr/bin/env python
# -*- coding: utf-8 -*-
import csv
import numpy as np
from sklearn import linear_model, datasets, metrics
from sklearn.neural_network import BernoulliRBM
from sklearn.pipeline import Pipeline

f = open("/Users/Helicopter/Desktop/Big_Data_Project/origin/TweetsAll.csv", "r")
reader = csv.reader(f,delimiter = ",")
data = list(reader)
num_twi = len(data)
num_word = len(data[0]) - 1
#print num_twi, num_word

X = np.zeros((num_twi, num_word))
Y = np.zeros(num_twi)
i = 0
for line in data:
	Y[i] = line[0]
	for j in range(1, num_word):
		X[i, j] = line[j]
	i += 1

print len(X), len(Y)

X_train = np.zeros((12000, num_word))
Y_train = np.zeros(12000)

X_test = np.zeros((3775, num_word))
Y_test = np.zeros(3775)

for no in range(len(X)):
	if no < 12000:
		X_train[no] = X[no]
		Y_train[no] = Y[no]
	else:
		X_test[no - 12000] = X[no]
		Y_test[no - 12000] = Y[no]

# Models we will use
logistic = linear_model.LogisticRegression()
rbm = BernoulliRBM(random_state=0, verbose=True)

classifier = Pipeline(steps=[('rbm', rbm), ('logistic', logistic)])

# Hyper-parameters. These were set by cross-validation,
# using a GridSearchCV. Here we are not performing cross-validation to
# save time.
rbm.learning_rate = 0.06
rbm.n_iter = 20
# More components tend to give better prediction performance, but larger
# fitting time
rbm.n_components = 100
logistic.C = 6000.0

# Training RBM-Logistic Pipeline
classifier.fit(X_train, Y_train)

# Training Logistic regression
logistic_classifier = linear_model.LogisticRegression(C=100.0)
logistic_classifier.fit(X_train, Y_train)

####################### Testing ############################

print()
print("Logistic regression using RBM features:\n%s\n" % (
    metrics.classification_report(
        Y_test,
        classifier.predict(X_test))))

print("Neural Network using raw pixel features:\n%s\n" % (
    metrics.classification_report(
        Y_test,
        logistic_classifier.predict(X_test))))




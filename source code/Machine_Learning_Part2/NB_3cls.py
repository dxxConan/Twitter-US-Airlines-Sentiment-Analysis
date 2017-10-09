#!/usr/bin/env python
# -*- coding: utf-8 -*-

import csv
import numpy as np
import re
from naiveBayesClassifier import tokenizer
from naiveBayesClassifier.trainer import Trainer
from naiveBayesClassifier.classifier import Classifier

def str_pre_process(str):
	stops =["a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"]	

	# remove '\n' first
	str = str.replace("\n"," ")
	# remove url
	str = re.sub(r"http://[a-zA-Z0-9-\./]* ", " ", str)
	str = re.sub(r"http://[a-zA-Z0-9-\./]*$", " ", str)
	str = re.sub(r"https://[a-zA-Z0-9-\./]* ", " ", str)
	str = re.sub(r"https://[a-zA-Z0-9-\./]*$", " ", str)
	# remove @
	str = re.sub(r"@[a-zA-Z0-9-\./]* ", " ", str)
	str = re.sub(r"@[a-zA-Z0-9-\./]*$", " ", str)
	# remove special character
	str = re.sub(r"[,-]", " ", str)
	str = re.sub(r"\'s ", " ", str)
	str = re.sub(r"[\(\)\_\'\"\#]", " ", str)
	# remove "..."
	str = re.sub(r"\.\.\.", " ", str)
	# remove "."
	str = re.sub(r"( \.|\. ) ", " ", str)
	# remove "." in the end
	str = re.sub(r"\.$", " ", str)
	# remove ":"
	str = re.sub(r": ", " ", str)
	# remove extra space
	str = re.sub(r" +", " ", str)
	# remove special word
	str = re.sub(r" (am|is|are|was|were|and|or) ", " ", str)
	str = re.sub(r" (to|of|on|at|in|the|from|that) ", " ", str)
	# remove tab
	str = re.sub(r"\t ", " ", str)
	# remove space in head or tail
	str = str.strip()

	return str

f = open("/Users/Helicopter/Desktop/Big_Data_Project/origin/Tweets.csv", "r")
reader = csv.reader(f,delimiter = ",")
data = list(reader)
num_twi = len(data)
trainer = Trainer(tokenizer)

print num_twi

train_twi = []
test_twi = []
true_cls = []
match = 0

print "Training..."

for no in range(12000):

	if data[no][1] == 'negative':
		cls = data[no][3]
	else:
		cls = data[no][1]

	twi_cont = str_pre_process(data[no][10])

	struct = {'text': twi_cont, 'category': data[no][1]}
	#print twi_cont, cls
	train_twi.append(struct)
	
for twi in train_twi:
	trainer.train(twi['text'], twi['category'])

model = Classifier(trainer.data, tokenizer)

print "Testing..."

for no in range(12000, num_twi):
	twi_cont = str_pre_process(data[no][10])
	classification = model.classify(twi_cont)
	#print classification,
	test_twi.append(classification)

	if data[no][1] == 'negative':
		cls = data[no][3]
	else:
		cls = data[no][1]

	true_cls.append(data[no][1])
	if classification[0][0] == data[no][1]:
		match += 1

sum = len(true_cls)

accuracy = match*1.0/sum
print accuracy


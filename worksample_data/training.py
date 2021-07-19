from os import walk
import re
import numpy as np
import pandas as pd
from sklearn.metrics import classification_report
from sklearn.feature_extraction.text import HashingVectorizer
from sklearn.model_selection import train_test_split
from sklearn.feature_selection import VarianceThreshold
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
import joblib
import time

def clean_file(file):
    """Function to clean file after reading it. readlines() required."""
    file_cleaned = []
    for line in file:
        line = (re.sub(r'(?m)^(\*|\(\*).*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^(/\*|\*).*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^(/\*|\*).*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^\#.*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^\\\\.*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^\<\-\-((.|\n)*)\n?', '',line.strip()))
        line = (re.sub(r'(?m)^//.*\n?', '',line.strip()))
        file_cleaned.append(line)
    file_cleaned = ' '.join(re.sub('[^a-zA-Z]', ' ',''.join(file_cleaned)).lower().split())
    return file_cleaned

def main():
    """This function is the main training loop for the program."""
    start_time = time.time()
    f = []
    for (dirpath, dirnames, filenames) in walk('./'):
        f.extend(dirnames)
        break
    print("Found folders:", *f)
    # Finding all files in folders, and making a dictinary {"filetype": list of files}
    files = {}
    for folder in f:
        for (dirpath, dirnames, filenames) in walk(folder):
            files[folder] = []
            for filename in filenames:
                try:
                    with open(folder + '/' + filename, 'r+') as file:
                        files[folder].append(file.readlines())
                except:
                    continue
    
    print("Found code files")
    # Cleaning files using regex
    for category in files:
        for i in range(len(files[category])):
            files[category][i] = clean_file(files[category][i])
    
    cats_len = [len(files[category]) for category in files]
    X = []
    for filetype in files:
        X+=files[filetype]
    y = []
    for i in range(len(files)):
        y+=[list(files.keys())[i]]*cats_len[i]
    
    X_train, X_test, y_train, y_test = train_test_split(X,y, test_size=0.2)
    hashing_vectorizer = HashingVectorizer(strip_accents="ascii", stop_words="english")
    X_train = hashing_vectorizer.fit_transform(X_train)
    X_test = hashing_vectorizer.transform(X_test)
    variance_threshold = VarianceThreshold(threshold=0.00009)

    variance_threshold.fit(X_train)
    X_train = variance_threshold.transform(X_train)
    X_test = variance_threshold.transform(X_test)
    
    classifier = RandomForestClassifier(n_estimators=200, max_depth=50)
    classifier.fit(X_train, y_train)
    print(classification_report(y_train, classifier.predict(X_train)))
    print(classification_report(y_test, classifier.predict(X_test)))
    joblib.dump(classifier, "rf.joblib")
    joblib.dump(hashing_vectorizer, "hv.joblib")
    joblib.dump(variance_threshold, "vt.joblib")
    print("Time taken for training is:", time.time()-start_time)
    
if __name__ == "__main__":
    main()
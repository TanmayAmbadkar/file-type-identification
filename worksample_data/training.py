from os import walk
import re
import numpy as np
import pandas as pd
from sklearn.metrics import classification_report
from sklearn.feature_extraction.text import HashingVectorizer
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.feature_selection import VarianceThreshold
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.linear_model import LogisticRegression
from sklearn.neighbors import KNeighborsClassifier
import seaborn as sns
import matplotlib.pyplot as plt
import joblib
import time

models = {
    'svc': {
        'model' : SVC(),
        'params' :{
            'C' : [1,10,20], 
            'kernel' : ['linear', 'rbf']
         }
    },
    'randomforest' : {
        'model' : RandomForestClassifier(),
        'params' :{
            'n_estimators' : [100,150,200],
            'criterion' : ['entropy']
        }
    },
    'LogiReg' : {
        'model' : LogisticRegression(),
        'params' : {
            'multi_class' : ['auto'],
            'C' : [1,5,10,15]
        }
    },
}

def clean_file(file):
    """Function to clean file after reading it. readlines() required."""
    file_cleaned = [] 
    for line in file: 
        line = (re.sub(r'(?m)^(\*|\(\*).*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^(/\*|\*).*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^\#.*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^\\\\.*\n?', '',line.strip()))
        line = (re.sub(r'(?m)^\<\-\-((.|\n)*)\n?', '',line.strip()))
        line = (re.sub(r'(?m)^//.*\n?', '',line.strip()))
        file_cleaned.append(line)
    file_cleaned = ' '.join(re.sub('[^a-zA-Z]', ' ',''.join(file_cleaned)).lower().split())
    return file_cleaned

def get_folders(path="./"):
    """Gets all extension folders from training folder."""
    folders = []
    for (dirpath, dirnames, filenames) in walk('./'):
        folders.extend(dirnames)
        break
    return folders
    
def get_files_folders(folders):
    """Gets all files from all selected folders."""
    files = {}
    for folder in folders:
        for (dirpath, dirnames, filenames) in walk(folder):
            files[folder] = []
            for filename in filenames:
                try:
                    with open(folder + '/' + filename, 'r+') as file:
                        files[folder].append(file.readlines())
                except:
                    continue
    return files

def apply_regex(files):
    """Apply regex to clean all files."""
    for category in files:
        for i in range(len(files[category])):
            files[category][i] = clean_file(files[category][i])
    
    return files

def prepare_dataset(files):
    """Preparing dataset from files."""
    cats_len = [len(files[category]) for category in files]
    sns.barplot(x=list(files.keys()), y=cats_len)
    plt.show()
    X = []
    for filetype in files:
        X+=files[filetype]
    y = []
    for i in range(len(files)):
        y+=[list(files.keys())[i]]*cats_len[i]
    
    return X, y

def prune_dataset(X, y):
    """Apply HashingVectorizer and VarianceThreshold."""
    X_train, X_test, y_train, y_test = train_test_split(X,y, test_size=0.2)
    hashing_vectorizer = HashingVectorizer(strip_accents="ascii", stop_words="english")
    X_train = hashing_vectorizer.fit_transform(X_train)
    X_test = hashing_vectorizer.transform(X_test)
    variance_threshold = VarianceThreshold(threshold=0.00001)
    variance_threshold.fit(X_train)
    X_train = variance_threshold.transform(X_train)
    X_test = variance_threshold.transform(X_test)
    
    return X_train, X_test, y_train, y_test, hashing_vectorizer, variance_threshold


def create_rf_model(X_train, X_test, y_train, y_test):
    """Creates a random forest model."""
    classifier = RandomForestClassifier(n_estimators=20, criterion="entropy", random_state=100)
    classifier.fit(X_train, y_train)
    #print(classification_report(y_train, classifier.predict(X_train)))
    print(classification_report(y_test, classifier.predict(X_test)))
    return classifier

def dump_model(classifier, hashing_vectorizer, variance_threshold):
    """Dumps the model files using joblib."""
    joblib.dump(classifier, "classifier.joblib")
    joblib.dump(hashing_vectorizer, "hv.joblib")
    joblib.dump(variance_threshold, "vt.joblib")

def cross_validation(X_train, X_test, y_train, y_test):
    """Cross validation carried out on different models."""
    scores = pd.DataFrame(
        columns = (
            'model', 
            'best_params',
            'best_score_train',
            'best_score_test'
        )
    )
    for model in models:
        model_grid = GridSearchCV(
            models[model]['model'],
            models[model]['params'],
            cv=10,
            return_train_score =False
        )
        model_grid.fit(X_train, y_train)
        scores.loc[len(scores)]=[
            model,
            str(model_grid.best_params_),
            model_grid.best_score_,
            model_grid.score(X_test,y_test)
        ]
        
    print(scores)

def main():
    """This function is the main training loop for the program."""
    start_time = time.time()
    folders = get_folders()
    print("Found folders:", *folders)
    # Finding all files in folders, and making a dictinary {"filetype": list of files}
    files = get_files_folders(folders)
    print("Found code files")
    # Cleaning files using regex
    print("Applying Regular expression to clean files")
    files = apply_regex(files)
    print("Preparing dataset for training")
    X,y = prepare_dataset(files)
    print("Applying hashing_vectorizer and variance_threshold")
    X_train, X_test, y_train, y_test, hashing_vectorizer, variance_threshold = prune_dataset(X,y)
    print("Cross validation for different models")
    #cross_validation(X_train, X_test, y_train, y_test)
    print("Training Random Forest Classifier")
    classifier_rf = create_rf_model(X_train, X_test, y_train, y_test)
    print("Dumping model")
    dump_model(classifier_rf, hashing_vectorizer, variance_threshold)
    print("Time taken for training is:", time.time()-start_time)
    
if __name__ == "__main__":
    main()
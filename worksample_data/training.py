from os import walk
import re
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
import joblib

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
    file_cleaned = re.sub('[^a-zA-Z]', ' ',''.join(file_cleaned)).lower().split()
    return file_cleaned

def freq(words):
    """Needed for preprocessing for training only."""    
    set_words = set(words)
    freq = {}
    for i in set_words:
  
         freq[i] = words.count(i)
    
    return freq

def avg(words):
    """Needed for preprocessing for training only."""    
    count = 0
    for word in words:
        count+=words[word]
    
    count = count/len(words)
    
    new_words = []
    for word in words:
        if words[word]>count*2:
            new_words.append(word)
            
    return new_words

def files_to_words(files):
    """Merging all words in all files to a single list."""
    words_file = {}
    for filetype in files:
        words_file[filetype] = []
        
        for file in files[filetype]:
            words_file[filetype]+=file
    return words_file

def main(): 
    f = []
    for (dirpath, dirnames, filenames) in walk('./'):
        f.extend(dirnames)
        break
    f.remove(".ipynb_checkpoints")
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
    
    print("Cleaned files to keep keywords and important words only")
    # Creating a list of words for each file type
    words_file = files_to_words(files)
    words_file_freq = {}
    
    # Filtering words that have frequency less than average frequency
    for filetype in files:
        words_file_freq[filetype] = avg(freq(words_file[filetype]))
    
    print("Filtered words to get only maximum frequency words")
    # Creating columns for dataframe from words
    columns = []
    for word in words_file_freq:
        columns+=words_file_freq[word]
    
    columns.append('class_output')
    dataframe = pd.DataFrame(columns = set(columns))
    
    # Appending values to dataframe
    i = 0
    for category in files:
        for file in files[category]:
            dataframe.loc[i] = np.zeros(len(set(columns)), dtype=int)
            for word in file:
                if word in columns:
                    dataframe[word][i]+=1
            dataframe['class_output'][i]=category
            i+=1
            if i%250==0:
                print(i)
    
    print("Appended words to get only maximum frequency words")
    X = dataframe.drop('class_output', axis=1)
    y = dataframe['class_output']
    X_train, X_test, y_train, y_test = train_test_split(X,y, test_size=0.2)
    classifier = RandomForestClassifier(n_estimators=200, max_depth=50)
    classifier.fit(X_train, y_train)
    print(classification_report(y_train, classifier.predict(X_train)))
    print(classification_report(y_test, classifier.predict(X_test)))
    joblib.dump(classifier, "rf.joblib")
    
    test_dataframe = pd.DataFrame(columns = set(columns))
    test_dataframe.drop('class_output', axis=1, inplace=True)
    
    test_dataframe.to_csv("test_df.csv", index_col = False)
    
if __name__ == "__main__":
    main()
from flask import Flask, request, jsonify
import joblib
import pandas as pd
import re
import numpy as np
import os
import time
import tracemalloc


app = Flask(__name__)
df = pd.read_csv("test_df.csv")
classifier = joblib.load("rf.joblib")

@app.route('/')
def hello():
    return "Hello World!"
    
@app.route('/predict', methods=["POST"])
def predict():
    
    file = request.files["file"]
    test_file = file.readlines()
    for i in range(len(test_file)):
        test_file[i] = test_file[i].decode("utf-8")
    file_cleaned = clean_file(test_file)
    df.loc[0] = np.zeros(len(set(df.columns)), dtype=int)
    for word in file_cleaned:
        if word in df.columns:
            df[word][0]+=1
            
    pred = classifier.predict(df)
    return jsonify({"file_type": pred[0]})

@app.route('/predict_stats', methods=["GET"])
def predict_stats():
    
    tracemalloc.start()
    start = time.time()
    list_files = []
    for file in os.listdir("stat_files"):
        with open("stat_files/"+file) as f:
            list_files.append(f.readlines())
    
    for i in range(len(list_files)):
        list_files[i] = clean_file(list_files[i])
    
    i=0
    for file_cleaned in list_files:
        df.loc[i] = np.zeros(len(set(df.columns)), dtype=int)

        for word in file_cleaned:
            if word in df.columns:
                df[word][i]+=1
        i+=1
    
    pred = classifier.predict(df)
    time_required = time.time() - start
    memory = tracemalloc.get_traced_memory()
    tracemalloc.stop()
    return jsonify(
        {
            "predictions_per_sec": 6/time_required,
            "memory": f"{memory[1]/(6*1024)} kb",
            "unit": "kb",
            "count": 6,
        }
    )



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
    
    
if __name__ == "__main__":
    
    app.run(host="0.0.0.0", port=5000, threaded=True, debug=True)

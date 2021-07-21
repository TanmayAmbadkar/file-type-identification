from flask import Flask, request, jsonify
import joblib
import pandas as pd
import re
import numpy as np
import os
import time
import tracemalloc


app = Flask(__name__)
classifier = joblib.load("classifier.joblib")
hashing_vectorizer = joblib.load("hv.joblib")
variance_threshold = joblib.load("vt.joblib")

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
    input_data = hashing_vectorizer.transform([file_cleaned])
    input_data = variance_threshold.transform(input_data)
    
    pred = classifier.predict(input_data)
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
    X = hashing_vectorizer.transform(list_files)
    print(X.shape)
    X = variance_threshold.transform(X)
    

    pred = classifier.predict(X)
    time_required = time.time() - start
    memory = tracemalloc.get_traced_memory()
    tracemalloc.stop()
    return jsonify(
        {
            "predictions_per_sec": len(list_files)/time_required,
            "memory": f"{memory[0]/(6*1024)} kb",
            "unit": "kb",
            "count": len(list_files),
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
    file_cleaned = ' '.join(re.sub('[^a-zA-Z]', ' ',''.join(file_cleaned)).lower().split())
    return file_cleaned
    
    
if __name__ == "__main__":
    
    app.run(host="0.0.0.0", port=5000, threaded=True, debug=True)

# File Type Identification using Machine Learning

In the given project, we can identify mislabelled files of certain types using a Random Forest Classifier trained on keywords of these languages, resulting in 99% accuracy.

The following file types can be recognised right now:
1. csproj
2. jenkinsfile
3. rexx
4. mak
5. ml
6. kt

There are 2 folders in this project
1. worksample_data
2. flask_files

## Worksample_data

This folder has the training files and the python script which can be used to train the classifier.

To add more languages, add a folder named as the language name, and add all files in that folder. The python script will generate 2 files:
1. rf.joblib: This is the Random Forest Classifier
2. test_df.csv: This is the empty dataframe containing the columns required by the classifier

Place these files in the flask_files folder after running the python script

## Flask_files

This folder has the flask api for testing the model. The following APIs are available

1. '/': returns hello world which is used to test if API is working or not. **GET**
2. 'predict': Takes in test file with form data value 'file' to predict which file type it is.**POST**
3. 'predict_stats': Provides the prediction speed, along with memory usage in kilobytes, averaged over 6 files. **GET**  

# How to run?

To run this project, create a new environment and write the following commands
```
pip install -r requirements.txt
```

To run the training script:
```
cd worksample_data
python training.py
```

To run the flask file
```
cd flask_files
python main.py
```
In a separate terminal, run
```
curl http://localhost:5000/predict_stats
```

You should receive the output for the stats.

import datetime
import utils
import pandas as pd
import pickle as pkl
import matplotlib.pyplot as plt
import datetime
import config
import os

from sklearn.linear_model import LogisticRegression
# y_pred = clf()
class StockData(object):
    def __init__(self, symbol):
        self.symbol = symbol
        self.start_date = '1980-01-01'
        self.current_date = str(datetime.date.today())
    
    def get_from_quandl(self, save=True):
        current_date = str(datetime.date.today())
        data = utils.download_stock(self.symbol, self.start_date, current_date, save)
        self.data = data
        
        return
    
    def retrieve_data(self, path, name):
        data = pd.read_csv(path + '/' + name, index_col= 'Date')
        data.index = pd.to_datetime(data.index)
        self.data = data
        
        return
    
    def update_to_latest(self):
        current_date = datetime.date.today()
        self.updated_data = utils.get_updated_data(self.data, current_date, self.symbol)
        self.data = utils.append_data(self.data, self.updated_data, self.symbol, save=True)
        latest_date = str(self.data.index[-1].to_pydatetime()).split(' ')[0]
        latest_date = datetime.datetime.strptime(latest_date, '%Y-%m-%d').date()
        self.latest_date = str(latest_date)
        self.latest_data = self.data.iloc[-1]
        
        return
    
    def make_classifier(self, save=True):
        data = self.data.copy()
        data_for_training = utils.simple_feature_engineer(data)
        data_for_training, label = utils.make_label(data_for_training)
        logreg = LogisticRegression()
        logreg.fit(data_for_training, label)
        
        if save:
            name = self.symbol + '_clf.pkl'
            path = config.path_to_clf + name
            with open(path, 'wb') as f:
                pkl.dump(logreg, f)
                
        self.clf = logreg
        
        return
        
    def load_classifier(self, path, name):
        with open(path + '/' + name, 'rb') as f:
            clf = pkl.load(f)
                    
        self.clf = clf
        
        return
    
    def latest_prediction(self):
        data_copy = self.data.copy().iloc[0:len(self.data)-1, :]
        prepared_input = utils.prepare_predicted_input(data_copy, self.latest_data)
        pred = self.clf.predict(prepared_input)
        self.latest_prediction = pred
        
        return
    
    def new_prediction(self):
        prepared_input = utils.prepare_predicted_input(self.data, self.updated_data)
        pred = self.clf.predict(prepared_input)
        
        self.latest_prediction = pred[-1]
    
        return
    
    def plot_data(self):
        closing = self.data.close
        plt.ion()
        fig = plt.figure()
        ax1 = fig.add_subplot(111)
        line1, = ax1.plot(range(len(closing)), closing)
        plt.show()
    
def make(stock_name):
    data = StockData(stock_name)
    
    data.get_from_quandl(save=True)
    data.make_classifier(save=True)
        
    object_name = stock_name + '_obj.pkl'
    with open(config.path_to_obj + '/' + object_name, 'wb') as f:
        pkl.dump(data, f)
        f.close()
        
    return data

def load(stock_name):
    object_name = stock_name + '_obj.pkl'
    with open(config.path_to_obj + '/' + object_name, 'rb') as f:
        stock = pkl.load(f)
        f.close()
        
    stock.update_to_latest()
    
    return stock
    
    
    

from talib.abstract import *
from sklearn.linear_model import LogisticRegression
#from sklearn.model_selection import GridSearchCV
from bs4 import BeautifulSoup
import datetime
import urllib.request
import numpy as np
import pandas as pd
import matplotlib as plt
import pandas_datareader as pdr
import pickle as pkl
import config

def download_stock(symbol, start_date, end_date, save=True):
    stock = pdr.get_data_quandl(symbol, start_date, end_date)
    stock = stock[::-1]
    stock.columns = [s.lower() for s in stock.columns]
    stock = stock[['open', 'high', 'low', 'close', 'volume']]

    if save:
        name = symbol + '.csv'
        path = config.path_to_data + '/' + name
        stock.to_csv(path)

    return stock

def get_updated_data(df, current_date, symbol):
	# Check type of current_date
    if type(current_date) == str:
        current_date = datetime.datetime.strptime(current_date, '%Y-%m-%d').date()
    # get latest date in the dataframe
    latest_date = str(df.index[-1].to_pydatetime()).split(' ')[0]
    latest_date = datetime.datetime.strptime(latest_date, '%Y-%m-%d').date()
    if (latest_date == current_date) or (latest_date > current_date):
        return df
    link = 'https://finance.yahoo.com/quote/' + symbol +'/history?p=' + symbol
    with urllib.request.urlopen(link) as url:
        r = url.read()
    soup = BeautifulSoup(r, 'lxml')
    body_find = soup.body.find('div', attrs={'class':'Pb(10px) Ovx(a) W(100%)'})
    all_span = body_find.find_all('span')
    colnames = [str(all_span[i].text) for i in range(7)]
    # Remove AdjClose
    colnames.pop(5)
    # Rename Close
    colnames[4] = 'Close'
    data = []
    for i in range(7, len(all_span), 7):
        x = ''.join(str(all_span[i].text).split(','))
        Date = datetime.datetime.strptime(x, '%b %d %Y').date()
        if Date == latest_date:
            break
        Open = float(''.join(str(all_span[i+1].text).split(',')))
        High = float(''.join(str(all_span[i+2].text).split(',')))
        Low = float(''.join(str(all_span[i+3].text).split(',')))
        Close = float(''.join(str(all_span[i+4].text).split(',')))
        Volume = float(''.join(str(all_span[i+6].text).split(',')))
        data.append([Date, Open, High, Low, Close, Volume])
    data = np.array(data).reshape(-1, 6)
    new_df = pd.DataFrame(data, columns=colnames)
    new_df = new_df.set_index(new_df.Date)
    new_df = new_df.drop(['Date'], axis=1)
    new_df = new_df.iloc[::-1]
    new_df.columns = [s.lower() for s in new_df.columns]
#    df = df.append(new_df)
#    df.index =pd.to_datetime(df.index, format='%Y-%m-%d')
    
    return new_df

def simple_feature_engineer(stock):

    stock.columns = [s.lower() for s in stock.columns]
    stock['sma_10'] = SMA(stock, timeperiod=10) # simple moving average (trung binh 10 ngay)
    stock['macd'] = MACD(stock, fastperiod=12, slowperiod=26)['macd'] # moving average con div 
    stock['rsi'] = RSI(stock, timeperiod=10) # relative strength index nhin vao volume de xem ban thao hay mua vao ( ban thao va mua voi va )
    stock['delta_volume'] = stock.volume.diff(5) # delta volume cua 5 

    stock = stock.dropna()
    stock = stock.reset_index(drop=True)
    stock = stock[['sma_10', 'macd', 'rsi', 'close', 'delta_volume']]

    return stock

def make_label(stock):
    diff = stock.close.diff(1)
    diff = diff[1:]
    label = [(lambda i: 0 if i <= 0 else 1)(i) for i in diff]
    
    stock = stock.iloc[1:, :]
    
    return stock, label

def append_data(stock, new_stock_data, symbol, save=True):
    stock = stock.append(new_stock_data)
    stock.index = pd.to_datetime(stock.index, format='%Y-%m-%d')
    for i in stock.columns:
        stock[i] = pd.to_numeric(stock[i])
        
    if save:
        name = symbol + '.csv'
        path = config.path_to_data + '/' + name
        stock.to_csv(path)    
        
    return stock

def prepare_predicted_input(stock, new_stock_data):
    ini_len = len(new_stock_data)
    stock = stock.tail(40)
    stock = stock[['open', 'high', 'low', 'close', 'volume']]
    stock = stock.append(new_stock_data)
    stock_2 = stock.copy()
    for i in stock_2.columns:
        stock_2[i] = pd.to_numeric(stock_2[i])
    stock_prepared = simple_feature_engineer(stock_2)
    index = len(stock_prepared) - ini_len
    stock_prepared = stock_prepared.iloc[index:,:]
    return stock_prepared


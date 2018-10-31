import Stock
import os
import config
import pickle as pkl
import datetime
import sys
from termcolor import colored
import matplotlib.pyplot as plt

if __name__ == '__main__':
    if not os.path.isfile(config.path_stockList):
        stockList = []
        with open(config.path_stockList, 'wb') as f:
            pkl.dump(stockList, f)
            f.close()
            
    with open(config.path_stockList, 'rb') as f:
        stockList = pkl.load(f)
        f.close()
        
    stock_name = sys.argv[1]
    if stock_name not in stockList:
        stockList.append(stock_name)
        
        stock = Stock.make(stock_name)
        stock.update_to_latest()
        if len(stock.updated_data) == 0:
            stock.latest_prediction()
        else:
            stock.new_prediction()
            
        if stock.latest_prediction == 0:
            trend = 'downtrend'
        else:
            trend = 'uptrend'
            
        print('The market will be ' + trend + ' tomorrow')
        
        with open(config.path_stockList, 'wb') as f:
            pkl.dump(stockList, f)
            f.close()
    
    else:
        stock = Stock.load(stock_name)
        if len(stock.updated_data) == 0:
            stock.latest_prediction()
        else:
            stock.new_prediction()
            
        if stock.latest_prediction == 0:
            trend = 'downtrend'
        else:
            trend = 'uptrend'
            
        print('The market will be ' + trend + ' tomorrow')
    plt.ion()
    fig = plt.figure()
    ax1 = fig.add_subplot(111)
    line1, = ax1.plot(stock.data.close)
    plt.show()
    plt.xlabel('Time')
    plt.ylabel('Price')
    plt.title('Historical data of ' + '{}'.format(stock_name))
    plt.pause(30)        
    
    

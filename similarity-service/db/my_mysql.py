from sqlalchemy.orm import sessionmaker
from sqlalchemy import create_engine

url = "mysql+mysqlconnector://root:NJUseLong995@124.221.127.36:3306/springbootjlvpC"
engine = create_engine(url, pool_size=5)
Session = sessionmaker(bind=engine)
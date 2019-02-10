from github import Github
from itertools import chain
from src.fetch_user_data import user_search, repo_search
from pprint import pprint
import json

g = Github("842e4feafe2d5b3e33173aa0c048dbff0c3d4cfa")

with open('to_search.json') as f:
    dev_input = json.load(f)

    pprint(dev_input)
    print("hi")
    print(dev_input["keywords"])

list1 = user_search("java")
list2 = repo_search("java", search_filters="Compiler")

zipper_list = list(chain.from_iterable(zip(list1, list2)))

for dev in zipper_list:
    print(dev)

json_string = json.dumps([ob.__dict__ for ob in zipper_list])
f = open("dev_data.json", "w")
f.write(json_string)

from github import Github
from src.dev_class import Developer

g = Github("842e4feafe2d5b3e33173aa0c048dbff0c3d4cfa")


def user_search(lang):
    user_paginated_list = g.search_users("language:" + lang, followers=">10").get_page(0)
    user_list = []
    for dev in user_paginated_list:
        if dev.name is None:
            continue
        user_list.append(Developer(login=dev.login, avatar_url=dev.avatar_url, name=dev.name, company=dev.company,
                                   blog=dev.blog,
                                   location=dev.location, email=dev.email, hireable=dev.hireable,
                                   public_repos=dev.public_repos,
                                   followers=dev.followers))
    return user_list


def repo_search(plang="None", search_filters=None):
    repos = g.search_repositories("language:" + plang + " " + search_filters)
    user_list = []
    max_stargazers = 0
    for repo in repos:
        if (max_stargazers * 5 < (repo.stargazers_count)):
            max_stargazers = (repo.stargazers_count) / 5
        if repo.stargazers_count < max_stargazers:
            return user_list
        repo_contributors = repo.get_contributors()
        for dev in repo_contributors:
            if dev.contributions < 70:
                continue
            user_list.append(Developer(login=dev.login, avatar_url=dev.avatar_url, name=dev.name, company=dev.company,
                                       blog=dev.blog,
                                       location=dev.location, email=dev.email, hireable=dev.hireable,
                                       public_repos=dev.public_repos,
                                       followers=dev.followers))

    return user_list


from github import Github
import time

g = Github("842e4feafe2d5b3e33173aa0c048dbff0c3d4cfa")


class Developer(object):

    def __init__(self, login=None, avatar_url=None, name=None, company=None, blog=None, location=None, email=None,
                 hireable=None, public_repos=None, followers=None, url=None):
        self.login = login
        self.avatar_url = avatar_url
        self.name = name
        self.company = company
        self.blog = blog
        self.location = location
        self.email = email
        self.hireable = hireable
        if not self.hireable:
            self.hireable = "False"
        self.public_repos = public_repos
        self.followers = followers
        self.url = "https://github.com/" + login

    def __eq__(self, other):
        return self.login == other.login

    # def user_repo_languages(self):
    #
    #     repo_list = g.get_user(self.user).get_repos()
    #     repo_size = 0
    #     for repo in repo_list:
    #         start = time.time()
    #         languages = repo.get_languages()
    #         end = time.time() - start
    #         print(end)
    #         for lang_values in languages.values():
    #             repo_size += lang_values
    #         for lang_keys in languages.keys():
    #             self.repo_languages[lang_keys] = self.repo_languages.get(lang_keys, 0) + languages.get(
    #                 lang_keys) / repo_size
    #
    #     print(end)

    def user_repo_languages(self):
        start = time.time()
        repo_list = g.get_user(self.user).get_repos().get_page(0)
        end = time.time() - start
        print(end)
        for repo in repo_list:
            self.repo_languages[repo.language] = self.repo_languages.get(repo.language, 0) + 1

    def __str__(self):
        return "User: {login}\nName: {name}\nRepos: {public_repos}\nHireable: {hireable}\n".format(login=self.login,
                                                                                                   name=self.name,
                                                                                                   public_repos=self.public_repos,
                                                                                                   hireable=self.hireable)

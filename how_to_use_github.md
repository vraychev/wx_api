#备留一份github 协同开发的手册

[使用git和github进行协同开发流程](http://www.kuqin.com/shuoit/20141213/343854.html)  
这个地址大家可以看看   

1. 登录[Github](https://github.com)  
2. Fork[wx_api](https://github.com/piggsoft/wx_api)
3. clone 到本地
4. 加入上游仓库源，```>> git remote add upstream https://github.com/piggsoft/wx_api```
5. 切换到dev分支， ```>> git checkout dev ```
6. 在开发分支下建立自己的开发分支 ```>> git checkout -b your_branch_name ```
7. 进行开发测试
8. 添加未加入版本库的文件 ```>> git add . ```
9. 进行提交 ```>> git commit -m "commit message"```
10. 重复迭代，直至完成
11. 回到dev分支 ```>> git checkout dev ```, 合并你自己的开发分支 ```>> git merge --no-ff your_branch_name ```
12. 确认无误，删除自己创立的分支 ```>> git branch -d your_branch_name```
13. 提交到自己fork的远程库中 ```>> git push origin dev ```
14. 在[Github](https://github.com) 页面中进行pull requests
15. 更新fork项目，```>> git check dev ```  
    ```>> git remote update upstream ```  
    ```>> git rebase upstream/dev ```
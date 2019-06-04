<c:if test="${param.showonly==true}">
    <div class="reviewDivlistReviews">
        <c:forEach items="${reviews}" var="r">
            <div class="reviewDivlistReviewsEach">
                <div class="reviewDate"><fmt:formatDate value="${r.createDate}" pattern="yyyy-MM-dd"/></div>
                <div class="reviewContent">${r.content}</div>
                <div class="reviewUserInfo pull-right">${r.user.anonymousName}<span class="reviewUserInfoAnonymous">(匿名)</span></div>
            </div>
        </c:forEach>
    </div>
</c:if>